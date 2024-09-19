package labs;

import org.noear.liquor.CodeSpec;
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

        Class<?> clazz1 = scriptEvaluator.getClazz("System.out.println(\"hello word\");");
        clazz1.getMethods()[0].invoke(null);

        CodeSpec code1 = new CodeSpec("class Demo {\n" +
                "            public String hello(String word) {\n" +
                "                return word;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        Demo demo = new Demo();\n" +
                "        return demo.hello(\"noear\");").returnType(String.class);

        System.out.println(scriptEvaluator.evaluate(code1)); //直接执行

        clazz1 = scriptEvaluator.getClazz(code1);
        System.out.println(clazz1.getMethods()[0].invoke(null)); //或转类再执行

        //////////////////////////

        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
        System.out.println(expressionEvaluator.evaluate("1+1"));
        System.out.println(expressionEvaluator.evaluate("1+1")); //cached
        System.out.println(expressionEvaluator.evaluate("1+2"));

        Class<?> clazz2 = expressionEvaluator.getClazz("1+1");
        System.out.println(clazz2.getMethods()[0].invoke(null));
    }
}
