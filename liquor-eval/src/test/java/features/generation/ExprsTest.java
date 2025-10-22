package features.generation;

import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.Exprs;

import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 表达式专用测试（必须有返回值）
 */
class ExprsTest {

    @Test
    void testExprsMustHaveReturnValue() {
        // Exprs 必须要有返回值
        Object result = Exprs.eval("10 * 20");
        assertEquals(200, result);
    }

    @Test
    void testComplexExpression() {
        Map<String, Object> context = new HashMap<>();
        context.put("price", 100.0);
        context.put("quantity", 3);
        context.put("discount", 0.1);

        Object result = Exprs.eval("price * quantity * (1 - discount)", context);
        assertEquals(270.0, result);
    }

    @Test
    void testTernaryOperator() {
        Map<String, Object> context = new HashMap<>();
        context.put("age", 17);

        Object result = Exprs.eval("age >= 18 ? \"adult\" : \"minor\"", context);
        assertEquals("minor", result);
    }

    @Test
    void testMethodCallInExpression() {
        Map<String, Object> context = new HashMap<>();
        context.put("text", "hello world");

        Object result = Exprs.eval("text.toUpperCase()", context);
        assertEquals("HELLO WORLD", result);
    }

    @Test
    void testArrayLength() {
        Map<String, Object> context = new HashMap<>();
        context.put("array", new int[]{1, 2, 3, 4, 5});

        Object result = Exprs.eval("array.length", context);
        assertEquals(5, result);
    }
}