package features;

import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.Exprs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author noear 2024/9/26 created
 */
public class LockTest {
    @Test
    public void test() throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();

        System.out.println(Exprs.eval(new CodeSpec("1+1").cached(false)));
        int count = 100;

        CountDownLatch countDownLatch = new CountDownLatch(count);

        for (int i = 0; i < count; i++) {
            executor.submit(() -> {
                try {
                    Object tmp = Exprs.eval(new CodeSpec("1000+1").cached(false));
                    System.out.println(Thread.currentThread().getName() + "::" + tmp);
                    countDownLatch.countDown();
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            });
        }

        countDownLatch.await();
        assert countDownLatch.getCount() == 0;
    }
}
