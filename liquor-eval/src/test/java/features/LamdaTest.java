package features;

import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.Scripts;
import org.noear.snack4.ONode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author noear 2025/2/18 created
 */
public class LamdaTest {
    @Test
    public void case1() throws Exception {
        Map<String, Object> argsMap = new LinkedHashMap();
        CodeSpec codeSpec = new CodeSpec("ONode siteNode = new ONode().asArray();\n" +
                "        siteNode.getArray().forEach(node -> {\n" +
                "\n" +
                "        });").imports(ONode.class);

        Scripts.eval(codeSpec, argsMap);
    }
}
