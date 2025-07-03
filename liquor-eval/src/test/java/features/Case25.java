package features;

import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.Exprs;
import org.noear.liquor.eval.LiquorEvaluator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author noear 2025/4/16 created
 */
public class Case25 {
    @Test
    public void test() throws Exception {
        //常量
        CodeSpec exp5 = new CodeSpec("Math.min(1,2)").imports(Math.class);
        System.out.println(Exprs.eval(exp5));


        //带上下文变量
        Map<String, Object> context = new HashMap<>();
        context.put("a", 1);
        context.put("b", 2);

        exp5 = new CodeSpec("Math.min(a,b)").imports(Math.class);
        System.out.println(Exprs.eval(exp5, context));
    }

    @Test
    public void test2() throws Exception {
        //常量
        CodeSpec exp6 = new CodeSpec("min(11,21)").imports("static java.lang.Math.*");
        System.out.println(Exprs.eval(exp6));


        //带上下文变量
        Map<String, Object> context = new HashMap<>();
        context.put("a", 11);
        context.put("b", 21);

        exp6 = new CodeSpec("min(a,b)").imports("static java.lang.Math.*");

        System.out.println(Exprs.eval(exp6, context));
    }
}
