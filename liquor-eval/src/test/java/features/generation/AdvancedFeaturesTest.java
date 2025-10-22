package features.generation;


import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.Exprs;
import org.noear.liquor.eval.Scripts;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 高级功能测试
 */
class AdvancedFeaturesTest {

    @Test
    void testImportStatement() {
        CodeSpec spec = new CodeSpec("Arrays.asList(1, 2, 3).size()")
                .imports(Arrays.class);

        Object result = Exprs.eval(spec);
        assertEquals(3, result);
    }

    @Test
    void testMultipleImports() {
        CodeSpec spec = new CodeSpec("Math.max(10, Math.min(5, 20))")
                .imports(Math.class);

        Object result = Exprs.eval(spec);
        assertEquals(10, result);
    }

    @Test
    void testComplexLogic() {
        String code = "int sum = 0;\n" +
                "            for (int i = 1; i <= 10; i++) {\n" +
                "                if (i % 2 == 0) {\n" +
                "                    sum += i;\n" +
                "                }\n" +
                "            }\n" +
                "            return sum;";

        Object result = Exprs.eval(code);
        assertEquals(30, result); // 2+4+6+8+10
    }

    @Test
    void testStringBuilderUsage() {
        CodeSpec spec = new CodeSpec("StringBuilder sb = new StringBuilder();\n" +
                "            for (int i = 0; i < 3; i++) {\n" +
                "                sb.append(\"Number: \").append(i).append(\"; \");\n" +
                "            }\n" +
                "            return sb.toString();").imports(StringBuilder.class);

        Object result = Exprs.eval(spec);
        assertEquals("Number: 0; Number: 1; Number: 2; ", result);
    }

    @Test
    void testConditionalLogic() {
        Map<String, Object> context = new HashMap<>();
        context.put("temperature", 25);
        context.put("isSummer", true);

        String code = "if (isSummer) {\n" +
                "                if (temperature > 30) {\n" +
                "                    return \"Hot\";\n" +
                "                } else if (temperature > 20) {\n" +
                "                    return \"Warm\";\n" +
                "                } else {\n" +
                "                    return \"Cool\";\n" +
                "                }\n" +
                "            } else {\n" +
                "                if (temperature > 20) {\n" +
                "                    return \"Mild\";\n" +
                "                } else {\n" +
                "                    return \"Cold\";\n" +
                "                }\n" +
                "            }";

        Object result = Exprs.eval(code, context);
        assertEquals("Warm", result);
    }
}