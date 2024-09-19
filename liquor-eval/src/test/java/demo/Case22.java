package demo;

import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.ExpressionEvaluator;
import org.noear.liquor.eval.ScriptEvaluator;

/**
 * @author noear 2024/9/19 created
 */
public class Case22 {
    public static void main(String[] args) throws Exception {
        ScriptEvaluator scriptEvaluator = new ScriptEvaluator();
        scriptEvaluator.setPrintable(true);

        CodeSpec code1 = new CodeSpec("class Demo {\n" +
                "      public String hello(String word) {\n" +
                "        return word;\n" +
                "      }\n" +
                "    }\n" +
                "\n" +
                "    Demo demo = new Demo();\n" +
                "    return demo.hello(name);") //name 为外部参数
                .parameters(new String[]{"name"}, new Class[]{String.class})
                .returnType(String.class);

        //直接执行
        System.out.println(scriptEvaluator.evaluate(code1, "noear"));
        assert "noear".equals(scriptEvaluator.evaluate(code1, "noear"));
        assert "solon".equals(scriptEvaluator.evaluate(code1, "solon"));

        //转类再执行
        Class<?> clazz1 = scriptEvaluator.getClazz(code1);
        System.out.println(clazz1.getMethods()[0].invoke(null, "noear"));

        ///////////////

        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
        expressionEvaluator.setPrintable(true);

        CodeSpec code2 = new CodeSpec("a+1").parameters(new String[]{"a"}, new Class[]{Integer.class});

        System.out.println(expressionEvaluator.evaluate(code2, 2));
        assert 3 == (int) expressionEvaluator.evaluate(code2, 2);

        ///////////////

        System.out.println(expressionEvaluator.evaluate(new CodeSpec("$0 + 22").parameters(Integer.class), 2));
        assert 24 == (int) expressionEvaluator.evaluate(new CodeSpec("$0 + 22").parameters(Integer.class), 2);
    }
}
