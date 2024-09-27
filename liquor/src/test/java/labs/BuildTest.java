package labs;

import org.noear.liquor.DynamicCompiler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author noear 2024/9/27 created
 */
public class BuildTest {
    public static void main(String[] args) throws Exception {
        String className = "com.demo.AClass";
        String classCode = "package com.demo;\n" +
                "\n" +
                "public class AClass {\n" +
                "\n" +
                "    public String say(String str){\n" +
                "        return \"hello\" + str;\n" +
                "    }\n" +
                "}";

        case1(className, classCode, 100);
        case1_2(className, classCode, 100);
        case2(className, classCode, 100);
        case3(className, classCode, 100);
        case4(className, classCode, 100);

        System.out.println("--------");

        case1(className, classCode, 1000); //   853ms/100;  8397ms/1000; （每次新建编译器）
        case1_2(className, classCode, 1000); // 391ms/100;  3460ms/1000; （每次新建编译器）多线程
        case2(className, classCode, 1000); //   418ms/100;  2328ms/1000; （复用编译器；每次新建类加载器）
        case3(className, classCode, 1000); //   284ms/100;  2433ms/1000; （复用编译器；复用类加载器；每次新编译）
        case4(className, classCode, 1000); //    35ms/100;   150ms/1000;  (复用编译器；复用类加载器；只一次编译)
    }

    public static void case1(String className, String classCode, int count) {
        long start = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            DynamicCompiler compiler = new DynamicCompiler();
            compiler.addSource(
                    className.replace("AClass", "AClass_" + i),
                    classCode.replace("AClass", "AClass_" + i));
            compiler.build();
        }

        System.out.println("case1:: " + (System.currentTimeMillis() - start));
    }

    public static void case1_2(String className, String classCode, int count) throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(count);

        long start = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            final String className2 = className.replace("AClass", "AClass_" + i);
            final String classCode2 = classCode.replace("AClass", "AClass_" + i);
            executor.submit(()->{
                DynamicCompiler compiler = new DynamicCompiler();
                compiler.addSource(className2,classCode2);
                compiler.build();
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();
        System.out.println("case1:: " + (System.currentTimeMillis() - start));
    }

    public static void case2(String className, String classCode, int count) {
        long start = System.currentTimeMillis();
        DynamicCompiler compiler = new DynamicCompiler();

        for (int i = 0; i < count; i++) {
            compiler.setClassLoader(compiler.newClassLoader());
            compiler.addSource(
                    className.replace("AClass", "AClass_" + i),
                    classCode.replace("AClass", "AClass_" + i));
            compiler.build();
        }

        System.out.println("case2:: " + (System.currentTimeMillis() - start));
    }

    public static void case3(String className, String classCode, int count) {
        long start = System.currentTimeMillis();
        DynamicCompiler compiler = new DynamicCompiler();

        for (int i = 0; i < count; i++) {
            compiler.addSource(
                    className.replace("AClass", "AClass_" + i),
                    classCode.replace("AClass", "AClass_" + i));
            compiler.build();
        }

        System.out.println("case2:: " + (System.currentTimeMillis() - start));
    }

    public static void case4(String className, String classCode, int count) {
        long start = System.currentTimeMillis();
        DynamicCompiler compiler = new DynamicCompiler();

        for (int i = 0; i < count; i++) {
            compiler.addSource(
                    className.replace("AClass", "AClass_" + i),
                    classCode.replace("AClass", "AClass_" + i));
        }
        compiler.build();

        System.out.println("case2:: " + (System.currentTimeMillis() - start));
    }
}