package labs;

import com.googlecode.aviator.AviatorEvaluator;
import org.noear.liquor.eval.Exprs;
import org.noear.solon.expression.snel.SnEL;

import java.util.function.Function;

/**
 * @author noear 2024/9/23 created
 */
public class BorderTest {
    public static void main(String[] args) throws Exception {
        int count = 1_000_000;

        caseNoCache(count, "aviator", AviatorEvaluator::execute);
//        caseNoCache(count, "liquor", Exprs::eval);
        caseNoCache(count, "snel", SnEL::eval);

        System.out.println("-----------------");

        caseInCache(count, "aviator", AviatorEvaluator::execute);
        caseInCache(count, "liquor", Exprs::eval);
        caseInCache(count, "snel", SnEL::eval);
    }

    private static void caseNoCache(int repetition, String name, Function<String, Object> action) {
        action.apply("1 + 1");
        action.apply("1 + 1");
        action.apply("1 + 1");

        long start = System.currentTimeMillis();

        for (int i = 0; i < repetition; i++) {
            action.apply("1 + 1" + i); //动态构建表达式（缓存无效）
        }

        System.out.println("caseNoCache - " + name + ": " + (System.currentTimeMillis() - start));
    }

    private static void caseInCache(int repetition, String name, Function<String, Object> action) {
        action.apply("1 + 1");
        action.apply("1 + 1");
        action.apply("1 + 1");

        long start = System.currentTimeMillis();

        for (int i = 0; i < repetition; i++) {
            action.apply("1 + 1"); //静态表达式（缓存有效）
        }

        System.out.println("caseInCache - " + name + ": " + (System.currentTimeMillis() - start));
    }
}
