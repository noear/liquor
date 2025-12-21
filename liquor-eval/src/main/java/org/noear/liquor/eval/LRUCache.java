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
import java.util.concurrent.atomic.LongAdder;
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
    private final NodeList<K, V> accessOrder;
    private final ReentrantLock evictionLock = new ReentrantLock();

    private static final int READ_BUFF_SIZE = 64;
    private static final int READ_BUFF_MASK = READ_BUFF_SIZE - 1;

    private final AtomicReferenceArray<Node<K, V>> readBuffer = new AtomicReferenceArray<>(READ_BUFF_SIZE);
    private final AtomicInteger readBufferIndex = new AtomicInteger(0);

    private final LongAdder sizeCounter = new LongAdder();

    public LRUCache(int capacity) {
        this.capacity = capacity;
        // 按照 0.75 负载因子预设初始大小，避免扩容带来的性能抖动
        this.data = new ConcurrentHashMap<>((int) (capacity / 0.75f) + 1);
        this.accessOrder = new NodeList<>();
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

        if (oldNode == null) {
            sizeCounter.increment();
        } else {
            // 标记旧节点失效，使其在后续 drain 或已在链表中的位置被跳过
            oldNode.retired = true;
        }

        // 写操作必须确保节点进入 accessOrder，否则该节点将永远无法被淘汰
        evictionLock.lock();
        try {
            drainBuffers();
            accessOrder.makeTail(newNode);
            evict();
        } finally {
            evictionLock.unlock();
        }
    }

    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        // 利用 CHM 的原子性保证 V 只会被计算一次
        Node<K, V> node = data.computeIfAbsent(key, k -> {
            V val = mappingFunction.apply(k);
            if (val == null) return null;
            sizeCounter.increment();
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
            sizeCounter.decrement();

            // 尝试从链表中物理移除以加速 GC
            if (evictionLock.tryLock()) {
                try {
                    accessOrder.remove(node);
                } finally {
                    evictionLock.unlock();
                }
            }
        }
    }

    private void recordAccess(Node<K, V> node) {
        int idx = readBufferIndex.getAndIncrement() & READ_BUFF_MASK;

        // 如果该槽位为空，存入当前节点。若不为空，说明 buffer 忙，放弃本次更新（LRU 允许微小偏差）
        readBuffer.compareAndSet(idx, null, node);

        if (idx == 0) {
            tryDrain();
        }
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
            // 原子性取出并重置为 null，确保每个节点只被处理一次
            Node<K, V> node = readBuffer.getAndSet(i, null);
            if (node != null && !node.retired) {
                accessOrder.makeTail(node);
            }
        }
    }

    private void evict() {
        // 虽然 sum() 相对 AtomicInteger.get() 慢，但在锁保护下且仅淘汰时调用，性能可控
        while (sizeCounter.sum() > capacity) {
            Node<K, V> oldest = accessOrder.removeHead();
            if (oldest != null) {
                // 使用 key + value(oldest) 双重检查删除，防止删错正在并发插入的新节点
                if (data.remove(oldest.key, oldest)) {
                    sizeCounter.decrement();
                }
            } else {
                break;
            }
        }
    }

    public int size() {
        return sizeCounter.intValue();
    }

    public void clear() {
        evictionLock.lock();
        try {
            data.clear();
            for (int i = 0; i < READ_BUFF_SIZE; i++) {
                readBuffer.set(i, null);
            }
            accessOrder.clear();
            sizeCounter.reset();
        } finally {
            evictionLock.unlock();
        }
    }

    // --- 内部数据结构 ---

    private static class Node<K, V> {
        final K key;
        final V value;
        // 关键：避免已移除节点常驻 Buffer 导致的内存泄漏
        volatile boolean retired = false;

        Node<K, V> prev, next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private static class NodeList<K, V> {
        private Node<K, V> head, tail;

        void makeTail(Node<K, V> node) {
            if (node == tail || node.retired) {
                return;
            }

            // 如果节点已在链表中，先将其剥离
            if (node.prev != null || node.next != null || node == head) {
                if (node.prev != null) node.prev.next = node.next;
                if (node.next != null) node.next.prev = node.prev;
                if (node == head) head = node.next;
                if (node == tail) tail = node.prev;
            }

            // 移到末尾
            node.prev = tail;
            node.next = null;
            if (tail == null) {
                head = tail = node;
            } else {
                tail.next = node;
                tail = node;
            }
        }

        Node<K, V> removeHead() {
            if (head == null) {
                return null;
            }
            Node<K, V> node = head;
            head = head.next;
            if (head == null) {
                tail = null;
            } else {
                head.prev = null;
            }
            node.prev = node.next = null;
            return node;
        }

        void remove(Node<K, V> node) {
            if (node.prev != null) node.prev.next = node.next;
            if (node.next != null) node.next.prev = node.prev;
            if (node == head) head = node.next;
            if (node == tail) tail = node.prev;
            node.prev = node.next = null;
        }

        void clear() {
            head = tail = null;
        }
    }
}