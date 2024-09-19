package labs;

import org.noear.liquor.eval.ExpressionEvaluator;
import org.noear.liquor.eval.ScriptEvaluator;

/**
 * @author noear
 * @since 1.1
 */
public class Case1 {
    public static void main(String[] args) throws Exception {
        ScriptEvaluator scriptEvaluator = new ScriptEvaluator();
        scriptEvaluator.evaluate("System.out.println(\"hello word\");");
        scriptEvaluator.evaluate("System.out.println(\"hello word\");"); //cached
        scriptEvaluator.evaluate("System.out.println(\"hello word1\");");

        //不推荐
        Class<?> clazz1 = scriptEvaluator.getClazz("System.out.println(\"hello word\");");
        clazz1.getMethods()[0].invoke(null);

        //////////////////////////

        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
        System.out.println(expressionEvaluator.evaluate("1+1"));
        System.out.println(expressionEvaluator.evaluate("1+1")); //cached
        System.out.println(expressionEvaluator.evaluate("1+2"));

        //不推荐
        Class<?> clazz2 = expressionEvaluator.getClazz("1+1");
        System.out.println(clazz2.getMethods()[0].invoke(null));
    }
}
