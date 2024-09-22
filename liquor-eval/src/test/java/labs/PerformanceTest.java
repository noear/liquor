package labs;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class PerformanceTest {
    public static void main(String[] args) {
        int size = 1000_000;
        Set<String> hashSet = new HashSet<>();
        Set<String> linkedHashSet = new LinkedHashSet<>();
        Set<String> treeSet = new TreeSet<>();

        // 测试 HashSet
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            hashSet.add("abasdfawefq wefc"+i);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("HashSet add time: " + (endTime - startTime) + "ms");

        // 测试 LinkedHashSet
        startTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            hashSet.add("aba sdfawefqwefc"+i);
        }
        endTime = System.currentTimeMillis();
        System.out.println("LinkedHashSet add time: " + (endTime - startTime) + "ms");

        // 测试 TreeSet
        startTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            hashSet.add("abasdfa wefqwefc"+i);
        }
        endTime = System.currentTimeMillis();
        System.out.println("TreeSet add time: " + (endTime - startTime) + "ms");
    }
}