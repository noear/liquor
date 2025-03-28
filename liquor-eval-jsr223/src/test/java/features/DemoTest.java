package features;

import org.junit.jupiter.api.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * @author noear 2024/9/23 created
 */
public class DemoTest {
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
}
