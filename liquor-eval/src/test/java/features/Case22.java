package features;

import org.junit.jupiter.api.Test;
import org.noear.liquor.Utils;
import org.noear.liquor.eval.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author noear 2024/9/19 created
 */
public class Case22 {
    @Test
    public void test() throws Exception {
        CodeSpec code1 = new CodeSpec("import java.util.HashMap;\n\n"+
                "    class Demo {\n" +
                "      public String hello(String word) {\n" +
                "        return word;\n" +
                "      }\n" +
                "    }\n" +
                "\n" +
                "    Demo demo = new Demo();\n" +
                "    return demo.hello(name);") //name 为外部参数
                .parameters(new ParamSpec("name", String.class))
                .imports(org.junit.jupiter.api.Test.class)
                .returnType(String.class)
                .cached(false);

        Map<String,Object> context = new HashMap<>();
        context.put("name","noear");

        //直接执行
        System.out.println(Scripts.eval(code1, Utils.asMap("name", "noear")));
        assert "noear".equals(Scripts.eval(code1, Utils.asMap("name","noear")));
        assert "solon".equals(Scripts.eval(code1, Utils.asMap("name","solon")));

        //转类再执行
        Execable execable1 = Scripts.compile(code1);
        System.out.println(execable1.exec( context));

        ///////////////


        CodeSpec code2 = new CodeSpec("a+1").parameters(new ParamSpec("a",Integer.class));

        System.out.println(Exprs.eval(code2, Utils.asMap("a",2)));
        assert 3 == (int) Exprs.eval(code2, Utils.asMap("a",2));

        ///////////////

        System.out.println(Exprs.eval(new CodeSpec("a + 22").parameters(new ParamSpec("a", Integer.class)), Utils.asMap("a",1)));
        assert 24 == (int) Exprs.eval(new CodeSpec("a + 22").parameters(new ParamSpec("a", Integer.class)), Utils.asMap("a",2));

        Map<String, Object> context1 = new HashMap<>();
        context1.put("aa", 3);
        context1.put("bb", 3L);
        System.out.println(Exprs.eval("aa + 22", context1));

        Map<String, Object> context2 = new HashMap<>();
        context2.put("aa", 4L);
        context1.put("bb", 4);
        System.out.println(Exprs.eval("aa + 22", context2));
    }
}
