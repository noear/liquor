package features;

import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.LiquorEvaluator;
import org.noear.liquor.eval.Scripts;
import org.noear.snack.ONode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author noear 2025/2/18 created
 */
public class LamdaTest {
    @Test
    public void case1() throws Exception {
        Map<String, Object> argsMap = new LinkedHashMap();
        CodeSpec codeSpec = new CodeSpec("ONode siteNode = ONode.newArray();\n" +
                "        siteNode.forEach(node -> {\n" +
                "\n" +
                "        });").imports(ONode.class);

        LiquorEvaluator.getInstance().printable(true);
        Scripts.eval(codeSpec, codeSpec.bind(argsMap));
    }
}
