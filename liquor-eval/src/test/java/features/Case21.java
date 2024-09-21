package features;

import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.ExpressionEvaluator;
import org.noear.liquor.eval.IExecutable;
import org.noear.liquor.eval.ScriptEvaluator;

/**
 * @author noear
 * @since 1.1
 */
public class Case21 {
    @Test
    public void test() throws Exception {
        ScriptEvaluator scriptEvaluator = ScriptEvaluator.getInstance();
        scriptEvaluator.eval("System.out.println(\"hello word\");");
        scriptEvaluator.eval("System.out.println(\"hello word\");"); //cached
        scriptEvaluator.eval("System.out.println(\"hello word---x\");");

        scriptEvaluator.compile("System.out.println(\"hello word1\");").exec();

        //不推荐
        IExecutable executable1 = scriptEvaluator.compile("System.out.println(\"hello word\");");
        executable1.exec();

        //////////////////////////

        ExpressionEvaluator expressionEvaluator = ExpressionEvaluator.getInstance();
        System.out.println(expressionEvaluator.eval("1+1"));
        System.out.println(expressionEvaluator.eval("1+1")); //cached
        System.out.println(expressionEvaluator.eval("1+2"));

        System.out.println(expressionEvaluator.compile("1+22222").exec());

        //不推荐
        IExecutable executable2 = expressionEvaluator.compile("1+1");
        System.out.println(executable2.exec());
    }
}
