/*
 * Copyright 2024 - 2025 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.noear.liquor.eval;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * 高性能 LRU 缓存 (基于 ConcurrentHashMap + 数组异步缓冲思想)
 *
 * @author noear
 * @since 1.3
 * @since 1.6
 */
public class LRUCache<K, V> {
    private final int capacity;
    private final ConcurrentHashMap<K, Node<K, V>> data;
    private final NodeList<K, V> accessOrder = new NodeList<>();
    private final ReentrantLock evictionLock = new ReentrantLock();

    private static final int READ_BUFF_SIZE = 64;
    private static final int READ_BUFF_MASK = READ_BUFF_SIZE - 1;
    private final AtomicReferenceArray<Node<K, V>> readBuffer = new AtomicReferenceArray<>(READ_BUFF_SIZE);
    private final AtomicInteger readBufferIndex = new AtomicInteger(0);
    private final AtomicInteger sizeCounter = new AtomicInteger(0);

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.data = new ConcurrentHashMap<>((int) (capacity / 0.75f) + 1);
    }

    public V get(K key) {
        Node<K, V> node = data.get(key);
        if (node != null) {
            recordAccess(node);
            return node.value;
        }
        return null;
    }

    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> oldNode = data.put(key, newNode);

        evictionLock.lock();
        try {
            if (oldNode == null) {
                sizeCounter.incrementAndGet();
            } else {
                oldNode.retired = true;
                accessOrder.remove(oldNode);
            }
            drainBuffers();
            accessOrder.makeTail(newNode);
            evict();
        } finally {
            evictionLock.unlock();
        }
    }

    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        Node<K, V> node = data.computeIfAbsent(key, k -> {
            V val = mappingFunction.apply(k);
            if (val == null) return null;
            sizeCounter.incrementAndGet();
            return new Node<>(k, val);
        });

        if (node != null) {
            recordAccess(node);
            return node.value;
        }
        return null;
    }

    public void remove(K key) {
        Node<K, V> node = data.remove(key);
        if (node != null) {
            node.retired = true;
            sizeCounter.decrementAndGet();
            if (evictionLock.tryLock()) {
                try {
                    accessOrder.remove(node);
                } finally {
                    evictionLock.unlock();
                }
            }
        }
    }

    public void clear() {
        evictionLock.lock();
        try {
            data.clear();
            for (int i = 0; i < READ_BUFF_SIZE; i++) readBuffer.set(i, null);
            accessOrder.clear();
            sizeCounter.set(0);
        } finally {
            evictionLock.unlock();
        }
    }

    private void recordAccess(Node<K, V> node) {
        int idx = (readBufferIndex.getAndIncrement() & 0x7FFFFFFF) & READ_BUFF_MASK;
        readBuffer.compareAndSet(idx, null, node);
        if (idx == 0) tryDrain();
    }

    private void tryDrain() {
        if (evictionLock.tryLock()) {
            try {
                drainBuffers();
                evict();
            } finally {
                evictionLock.unlock();
            }
        }
    }

    private void drainBuffers() {
        for (int i = 0; i < READ_BUFF_SIZE; i++) {
            Node<K, V> node = readBuffer.getAndSet(i, null);
            if (node != null && !node.retired) {
                accessOrder.makeTail(node);
            }
        }
    }

    private void evict() {
        while (sizeCounter.get() > capacity) {
            Node<K, V> oldest = accessOrder.removeHead();
            if (oldest != null) {
                if (data.remove(oldest.key, oldest)) {
                    sizeCounter.decrementAndGet();
                }
            } else {
                break;
            }
        }
    }

    public int size() { return sizeCounter.get(); }

    private static class Node<K, V> {
        final K key; final V value;
        volatile boolean retired = false;
        volatile Node<K, V> prev, next;
        Node(K key, V value) { this.key = key; this.value = value; }
    }

    private static class NodeList<K, V> {
        private Node<K, V> head, tail;

        void makeTail(Node<K, V> node) {
            if (node == tail || node.retired) return;
            remove(node);
            node.prev = tail;
            node.next = null;
            if (tail == null) head = tail = node;
            else { tail.next = node; tail = node; }
        }

        Node<K, V> removeHead() {
            if (head == null) return null;
            Node<K, V> node = head;
            remove(node);
            return node;
        }

        void remove(Node<K, V> node) {
            if (node.prev != null) node.prev.next = node.next;
            if (node.next != null) node.next.prev = node.prev;
            if (node == head) head = node.next;
            if (node == tail) tail = node.prev;
            node.prev = node.next = null;
        }

        void clear() { head = tail = null; }
    }
}