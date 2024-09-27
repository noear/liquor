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

        case11(scriptCode, true, 10);
        case12(scriptCode, true, 10);
        case13(scriptCode, true, 10);
        case14(scriptCode, true, 10);

        System.out.println("--------");
        scriptCode = "return 3 + 1;";

        case11(scriptCode, false, 1000); //   704ms/100;  4302ms/1000; (单线程，cached)
        case12(scriptCode, false, 1000); //   382ms/100;  2844ms/1000; (单线程)
        case13(scriptCode, false, 1000); //     2ms/100;    12ms/1000; (多线程，cached)
        case14(scriptCode, false, 1000); //   421ms/100;  2899ms/1000; (多线程)

    }

    public static void case11(String scriptCode, boolean printable, int count) throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            final String scriptCode2 = scriptCode.replace("1", String.valueOf(i));

            Object rst = Scripts.eval(new CodeSpec(scriptCode2).returnType(Object.class).cached(true));
            if (printable) {
                System.out.println("case11-r:: " + rst);
            }
        }

        System.out.println("case11:: " + (System.currentTimeMillis() - start));
    }

    public static void case12(String scriptCode, boolean printable, int count) throws Exception {
        long start = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            final String scriptCode2 = scriptCode.replace("1", String.valueOf(i));

            Object rst = Scripts.eval(new CodeSpec(scriptCode2).returnType(Object.class).cached(false));
            if (printable) {
                System.out.println("case12-r:: " + rst);
            }
        }

        System.out.println("case12:: " + (System.currentTimeMillis() - start));
    }

    public static void case13(String scriptCode, boolean printable, int count) throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(count);

        long start = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            final String scriptCode2 = scriptCode.replace("1", String.valueOf(i));

            executor.submit(() -> {
                try {
                    Object rst = Scripts.eval(new CodeSpec(scriptCode2).returnType(Object.class).cached(true));
                    if (printable) {
                        System.out.println("case13-r:: " + rst);
                    }

                    countDownLatch.countDown();
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            });
        }

        countDownLatch.await();
        System.out.println("case13:: " + (System.currentTimeMillis() - start));
    }

    public static void case14(String scriptCode, boolean printable, int count) throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(count);

        long start = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            final String scriptCode2 = scriptCode.replace("1", String.valueOf(i));

            executor.submit(() -> {
                try {
                    Object rst = Scripts.eval(new CodeSpec(scriptCode2).returnType(Object.class).cached(false));
                    if (printable) {
                        System.out.println("case14-r:: " + rst);
                    }

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