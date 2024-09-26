package features;

import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.Exprs;

/**
 * @author noear 2024/9/23 created
 */
public class CachedTest {
    @Test
    public void test2() throws Exception {
        ClassLoader classLoader1 = Exprs.compile(new CodeSpec("1+1"))
                .getMethod().getDeclaringClass().getClassLoader();

        ClassLoader classLoader2 = Exprs.compile(new CodeSpec("1+1"))
                .getMethod().getDeclaringClass().getClassLoader();

        System.out.println(classLoader1);
        System.out.println(classLoader2);

        assert classLoader1.equals(classLoader2);
    }
}
