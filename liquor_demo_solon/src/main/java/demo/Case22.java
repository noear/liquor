package demo;

import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.ExpressionEvaluator;
import org.noear.liquor.eval.ScriptEvaluator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author noear 2024/9/19 created
 */
public class Case22 {
    public static void main(String[] args) throws Exception {
        ScriptEvaluator scriptEvaluator = new ScriptEvaluator();
        scriptEvaluator.setPrintable(true);

        CodeSpec code1 = new CodeSpec("import java.util.HashMap;\n\n"+
                "    class Demo {\n" +
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
        System.out.println(scriptEvaluator.eval(code1, "noear"));
        assert "noear".equals(scriptEvaluator.eval(code1, "noear"));
        assert "solon".equals(scriptEvaluator.eval(code1, "solon"));

        //转类再执行
        Class<?> clazz1 = scriptEvaluator.getClazz(code1);
        System.out.println(clazz1.getMethods()[0].invoke(null, "noear"));

        ///////////////

        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
        expressionEvaluator.setPrintable(true);

        CodeSpec code2 = new CodeSpec("a+1").parameters(new String[]{"a"}, new Class[]{Integer.class});

        System.out.println(expressionEvaluator.eval(code2, 2));
        assert 3 == (int) expressionEvaluator.eval(code2, 2);

        ///////////////

        System.out.println(expressionEvaluator.eval(new CodeSpec("$0 + 22").parameters(Integer.class), 1));
        assert 24 == (int) expressionEvaluator.eval(new CodeSpec("$0 + 22").parameters(Integer.class), 2);

        Map<String, Object> bings = new HashMap<>();
        bings.put("aa", 3);
        System.out.println(expressionEvaluator.eval("aa + 22", bings));

        Map<String, Object> bings2 = new HashMap<>();
        bings2.put("aa", 4L);
        System.out.println(expressionEvaluator.eval("aa + 22", bings2));
    }
}
