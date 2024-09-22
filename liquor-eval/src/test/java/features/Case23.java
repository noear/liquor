package features;

import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.LiquorEvaluator;
import org.noear.liquor.eval.Scripts;

import java.util.HashMap;
import java.util.List;

/**
 * @author noear 2024/9/20 created
 */
public class Case23 {
    @Test
    public void test() throws Exception {
        LiquorEvaluator.getInstance().printable(true);

        CodeSpec code1 = new CodeSpec("import java.util.HashMap;\n" +
                "    import java.util.Collection;\n" +
                "    import java.util.Collection;\n" +

                "    class Demo extends HashMap<String, Object> {}\n" +
                "\n" +
                "    Demo demo = new Demo();\n" +

                "    if(demo instanceof Collection){\n" +
                "        return demo.size();\n" +
                "    } else{\n" +
                "        return 0;\n" +
                "    }") //name 为外部参数
                .returnType(Object.class)
                .imports(HashMap.class, List.class);

        System.out.println(Scripts.eval(code1));
    }
}
