package labs;

import org.noear.solon.expression.util.LRUCache;

import java.util.*;
import java.util.concurrent.*;

public class LRUComputeTest {

    private static final int CAPACITY = 1000;
    private static final int THREADS = 16;
    private static final int OPS_PER_THREAD = 100_000;
    private static final int KEY_RANGE = 2000; // 2倍容量，保证会有淘汰

    public static void main(String[] args) throws InterruptedException {
        LRUComputeTest test = new LRUComputeTest();

        System.out.println("--- 开始单线程测试 ---");
        test.runSingleThread();

        System.out.println("\n--- 开始多线程高并发测试 ---");
        test.runMultiThread();
    }

    // 模拟业务逻辑的计算函数
    private Integer computeLogic(Integer k) {
        // 模拟轻微计算开销
        return k.hashCode() * 31;
    }

    public void runSingleThread() {
        // 1. LinkedHashMap 包装版
        Map<Integer, Integer> lru1 = Collections.synchronizedMap(new LRUCache2<>(CAPACITY));
        // 2. 高性能并发版
        LRUCache<Integer, Integer> lru2 = new LRUCache<>(CAPACITY);

        // Warm up
        for(int i=0; i<1000; i++) {
            lru1.computeIfAbsent(i % KEY_RANGE, this::computeLogic);
            lru2.computeIfAbsent(i % KEY_RANGE, this::computeLogic);
        }

        measure("单线程 - Synchronized版", () -> {
            for (int i = 0; i < OPS_PER_THREAD * THREADS; i++) {
                lru1.computeIfAbsent(i % KEY_RANGE, this::computeLogic);
            }
        });

        measure("单线程 - 高性能并发版", () -> {
            for (int i = 0; i < OPS_PER_THREAD * THREADS; i++) {
                lru2.computeIfAbsent(i % KEY_RANGE, this::computeLogic);
            }
        });
    }

    public void runMultiThread() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);

        // 1. Synchronized版
        Map<Integer, Integer> lru1 = Collections.synchronizedMap(new LRUCache2<>(CAPACITY));
        measure("多线程 - Synchronized版", () -> execute(executor, (k) -> lru1.computeIfAbsent(k, this::computeLogic)));

        // 2. 高性能并发版
        LRUCache<Integer, Integer> lru2 = new LRUCache<>(CAPACITY);
        measure("多线程 - 高性能并发版", () -> execute(executor, (k) -> lru2.computeIfAbsent(k, this::computeLogic)));

        executor.shutdown();
    }

    private void execute(ExecutorService executor, java.util.function.Consumer<Integer> task) {
        CountDownLatch latch = new CountDownLatch(THREADS);
        for (int i = 0; i < THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < OPS_PER_THREAD; j++) {
                    task.accept(j % KEY_RANGE);
                }
                latch.countDown();
            });
        }
        try { latch.await(); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    private void measure(String label, Runnable run) {
        long start = System.nanoTime();
        run.run();
        long end = System.nanoTime();
        System.out.printf("%s 耗时: %.2f ms\n", label, (end - start) / 1_000_000.0);
    }
}