package features;

import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.LiquorEvaluator;
import org.noear.liquor.eval.jsr223.ScriptEngineImpl;

import javax.script.ScriptEngine;

/**
 * @author noear 2024/9/23 created
 */
public class Case1 {
    @Test
    public void test() throws Exception {
        LiquorEvaluator.getInstance().printable(true);
        ScriptEngine s = new ScriptEngineImpl();

        System.out.println(s.eval("return 1+1;"));
    }
}
