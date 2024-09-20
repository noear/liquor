package demo;

import org.noear.liquor.eval.ExpressionEvaluator;
import org.noear.liquor.eval.ScriptEvaluator;

/**
 * @author noear
 * @since 1.1
 */
public class Case21 {
    public static void main(String[] args) throws Exception {
        ScriptEvaluator scriptEvaluator = ScriptEvaluator.getInstance();
        scriptEvaluator.eval("System.out.println(\"hello word\");");
        scriptEvaluator.eval("System.out.println(\"hello word\");"); //cached
        scriptEvaluator.eval("System.out.println(\"hello word---x\");");

        scriptEvaluator.compile("System.out.println(\"hello word1\");").exec();

        //不推荐
        Class<?> clazz1 = scriptEvaluator.getClazz("System.out.println(\"hello word\");");
        clazz1.getMethods()[0].invoke(null);

        //////////////////////////

        ExpressionEvaluator expressionEvaluator = ExpressionEvaluator.getInstance();
        System.out.println(expressionEvaluator.eval("1+1"));
        System.out.println(expressionEvaluator.eval("1+1")); //cached
        System.out.println(expressionEvaluator.eval("1+2"));

        System.out.println(expressionEvaluator.compile("1+22222").exec());

        //不推荐
        Class<?> clazz2 = expressionEvaluator.getClazz("1+1");
        System.out.println(clazz2.getMethods()[0].invoke(null));
    }
}
