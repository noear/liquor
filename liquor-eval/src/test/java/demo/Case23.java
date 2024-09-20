package demo;

import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.ScriptEvaluator;

/**
 * @author noear 2024/9/20 created
 */
public class Case23 {
    public static void main(String[] args) throws Exception {
        ScriptEvaluator scriptEvaluator = new ScriptEvaluator();
        scriptEvaluator.setPrintable(true);

        CodeSpec code1 = new CodeSpec("import java.util.HashMap;\n" +
                "    import java.util.Collection;\n" +

                "    class Demo extends HashMap<String, Object> {}\n" +
                "\n" +
                "    Demo demo = new Demo();\n" +

                "    if(demo instanceof Collection){\n" +
                "        return demo.size();\n" +
                "    } else{\n" +
                "        return 0;\n" +
                "    }") //name 为外部参数
                .returnType(Object.class);

        System.out.println(scriptEvaluator.eval(code1));
    }
}
