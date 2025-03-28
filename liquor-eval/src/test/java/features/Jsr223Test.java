package features;

import org.junit.jupiter.api.Test;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.io.StringReader;

/**
 * @author noear 2024/9/23 created
 */
public class Jsr223Test {
    ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

    @Test
    public void case1() throws Exception {
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("liquor");

        String exp = "return 2*6-(6+5);";
        Object result = scriptEngine.eval(exp);
        System.out.println(exp + "=" + result);
        assert result instanceof Integer;
        assert ((Integer) result) == 1;

        Object result2 = scriptEngine.eval("return 1+1;");
        System.out.println(result2);
        assert ((Integer) result2) == 2;
    }

    @Test
    public void case2() throws Exception {
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("java");

        String exp = "return 2*6-(6+5);";
        Object result = scriptEngine.eval(exp);
        System.out.println(exp + "=" + result);
        assert result instanceof Integer;
        assert ((Integer) result) == 1;

        Object result2 = scriptEngine.eval("return 1+1;");
        System.out.println(result2);
        assert ((Integer) result2) == 2;
    }

    @Test
    public void case3() throws Exception {
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("java");

        String script = "if(a > 1){\n" +
                "\treturn 1;\n" +
                "}else{\n" +
                "\treturn 0;\n" +
                "}";


        Bindings context = new SimpleBindings();
        context.put("a", 1);

        Object result = scriptEngine.eval(script, context);

        System.out.println(result);
        assert ((Integer) result) == 0;

        context = new SimpleBindings();
        context.put("a", 2);
        result = scriptEngine.eval(script, context);

        System.out.println(result);
        assert ((Integer) result) == 1;


        context = new SimpleBindings();
        context.put("a", 2);
        result = scriptEngine.eval(new StringReader(script), context);

        System.out.println(result);
        assert ((Integer) result) == 1;
    }
}
