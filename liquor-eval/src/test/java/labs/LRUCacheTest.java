package labs;

import org.junit.jupiter.api.Test;
import org.noear.solon.expression.util.LRUCache;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author noear 2025/12/27 created
 *
 */
public class LRUCacheTest {
    @Test
    public void testSingleThreadPerformance() {
        int iterations = 1_000_000;

        // 测试 LinkedHashMap (LRU 模式)
        Map<Integer, Integer> lru1 = Collections.synchronizedMap(new LRUCache2<>(1000));
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            lru1.put(i % 2000, i);
            lru1.get(i % 2000);
        }
        long end = System.nanoTime();
        System.out.println("单线程 - LinkedHashMap 耗时: " + (end - start) / 1_000_000 + "ms");

        // 测试高性能并发类
        LRUCache<Integer, Integer> lru2 = new LRUCache<>(1000);
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            lru2.put(i % 2000, i);
            lru2.get(i % 2000);
        }
        end = System.nanoTime();
        System.out.println("单线程 - 高性能并发类 耗时: " + (end - start) / 1_000_000 + "ms");
    }

    @Test
    public void testMultiThreadPerformance() throws InterruptedException {
        int threadCount = 10;
        int opsPerThread = 100_000;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        // 1. 测试包装后的 LinkedHashMap (全局锁)
        Map<Integer, Integer> syncCache = Collections.synchronizedMap(new LRUCache2<>(1000));
        CountDownLatch latch1 = new CountDownLatch(threadCount);
        long start = System.nanoTime();
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                for (int j = 0; j < opsPerThread; j++) {
                    syncCache.put(j % 2000, j);
                    syncCache.get(j % 2000);
                }
                latch1.countDown();
            });
        }
        latch1.await();
        long end = System.nanoTime();
        System.out.println("多线程 - Synchronized LinkedHashMap 耗时: " + (end - start) / 1_000_000 + "ms");

        // 2. 测试高性能并发类 (无锁读 + 缓冲淘汰)
        LRUCache<Integer, Integer> fastCache = new LRUCache<>(1000);
        CountDownLatch latch2 = new CountDownLatch(threadCount);
        start = System.nanoTime();
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                for (int j = 0; j < opsPerThread; j++) {
                    fastCache.put(j % 2000, j);
                    fastCache.get(j % 2000);
                }
                latch2.countDown();
            });
        }
        latch2.await();
        end = System.nanoTime();
        System.out.println("多线程 - 高性能并发类 耗时: " + (end - start) / 1_000_000 + "ms");

        executor.shutdown();
    }
}
