package labs;

import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.Scripts;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author noear 2024/9/27 created
 */
public class BuildScriptTest {
    public static void main(String[] args) throws Exception {
        String scriptCode = "return 2 + 1;";

        case11(scriptCode, 10);
        case12(scriptCode, 10);
        case13(scriptCode, 10);
        case14(scriptCode, 10);

        System.out.println("--------");
        scriptCode = "return 3 + 1;";

        case11(scriptCode, 1000); //   704ms/100;  4302ms/1000; (单线程，cached)
        case12(scriptCode, 1000); //   382ms/100;  2844ms/1000; (单线程)
        case13(scriptCode, 1000); //     2ms/100;    12ms/1000; (多线程，cached)
        case14(scriptCode, 1000); //   421ms/100;  2899ms/1000; (多线程)

    }

    public static void case11(String scriptCode, int count) {
        long start = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            final String scriptCode2 = scriptCode.replace("1", String.valueOf(i));

            Scripts.compile(new CodeSpec(scriptCode2).returnType(Object.class).cached(true));
        }

        System.out.println("case11:: " + (System.currentTimeMillis() - start));
    }

    public static void case12(String scriptCode, int count) {
        long start = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            final String scriptCode2 = scriptCode.replace("1", String.valueOf(i));

            Scripts.compile(new CodeSpec(scriptCode2).returnType(Object.class).cached(false));
        }

        System.out.println("case12:: " + (System.currentTimeMillis() - start));
    }

    public static void case13(String scriptCode, int count) throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(count);

        long start = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            final String scriptCode2 = scriptCode.replace("1", String.valueOf(i));

            executor.submit(() -> {
                try {
                    Scripts.compile(new CodeSpec(scriptCode2).returnType(Object.class).cached(true));
                    countDownLatch.countDown();
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            });
        }

        countDownLatch.await();
        System.out.println("case13:: " + (System.currentTimeMillis() - start));
    }

    public static void case14(String scriptCode, int count) throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(count);

        long start = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            final String scriptCode2 = scriptCode.replace("1", String.valueOf(i));

            executor.submit(() -> {
                try {
                    Scripts.compile(new CodeSpec(scriptCode2).returnType(Object.class).cached(false));
                    countDownLatch.countDown();
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            });
        }

        countDownLatch.await();
        System.out.println("case14:: " + (System.currentTimeMillis() - start));
    }
}