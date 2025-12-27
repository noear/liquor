package labs;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache2<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public LRUCache2(int capacity) {
        super(capacity, 0.75f, true); // 最后一个参数true表示按照访问顺序排序
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity; // 当缓存大小超过容量时，移除最老的条目
    }
}