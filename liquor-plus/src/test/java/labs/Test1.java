package labs;

import org.noear.liquor.ExpressionEvaluator;
import org.noear.liquor.ScriptEvaluator;

/**
 * @author noear
 * @since 1.1
 */
public class Test1 {
    public static void main(String[] args) throws Exception {
        ScriptEvaluator scriptEvaluator = new ScriptEvaluator();
        scriptEvaluator.evaluate("System.out.println(\"hello word\");");
        scriptEvaluator.evaluate("System.out.println(\"hello word\");"); //cached
        scriptEvaluator.evaluate("System.out.println(\"hello word1\");");

        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
        System.out.println(expressionEvaluator.evaluate("1+1"));
        System.out.println(expressionEvaluator.evaluate("1+1")); //cached
        System.out.println(expressionEvaluator.evaluate("1+2"));
    }
}
