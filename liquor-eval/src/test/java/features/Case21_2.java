package features;

import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.*;

import java.util.Map;

/**
 * @author noear 2024/9/20 created
 */
public class Case21_2 {
    @Test
    public void test() throws Exception {
        // 数学运算 (Long)
        String exp1 = "1+2+3";
        Integer result = (Integer) Exprs.eval(exp1);
        System.out.println(result); // 6

        // 数学运算 (Double)
        String exp4 = "1.1+2.2+3.3";
        Double result2 = (Double) Exprs.eval(exp4);
        System.out.println(result2); // 6.6


        // 包含关系运算和逻辑运算
        String exp2 = "(1>0||0<1)&&1!=0";
        System.out.println(Exprs.eval(exp2)); // true


        // 三元运算
        String exp3 = "4 > 3 ? \"4 > 3\" : 999";
        System.out.println(Exprs.eval(exp3)); // 4 > 3


        CodeSpec exp5 = new CodeSpec("Math.min(1,2)").imports(Math.class);
        System.out.println(Exprs.eval(exp5));

        Scripts.eval(new CodeSpec("System.out.println(Math.min(11,21));").imports(Math.class));


        CodeSpec exp6 = new CodeSpec("min(22,12)").imports("static java.lang.Math.*");
        System.out.println(Exprs.eval(exp6));


        Map<String, Object> context = Maps.of();
        context.put("a", 1);
        context.put("b", 2);

        LiquorEvaluator.getInstance().printable(true);
        Exprs.eval("if (a < 0) { return b; } else { return 0; }", context);
    }
}
