package features.generation;

import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.Exprs;
import org.noear.liquor.eval.Scripts;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 基础功能测试
 */
class LiquorEvaluatorBasicTest {

    @Test
    void testSimpleExpression() {
        Object result = Exprs.eval("1 + 2 * 3");
        assertEquals(7, result);
    }

    @Test
    void testWithContext() {
        Map<String, Object> context = new HashMap<>();
        context.put("a", 10);
        context.put("b", 20);

        Object result = Exprs.eval("a + b", context);
        assertEquals(30, result);
    }

    @Test
    void testStringConcatenation() {
        Map<String, Object> context = new HashMap<>();
        context.put("name", "John");
        context.put("age", 25);

        Object result = Exprs.eval("name + \" is \" + age + \" years old\"", context);
        assertEquals("John is 25 years old", result);
    }

    @Test
    void testBooleanExpression() {
        Map<String, Object> context = new HashMap<>();
        context.put("score", 85);
        context.put("passingScore", 60);

        Object result = Exprs.eval("score >= passingScore", context);
        assertEquals(true, result);
    }

    @Test
    void testNullSafe() {
        // 测试空上下文
        Object result = Exprs.eval("1 + 1", Collections.emptyMap());
        assertEquals(2, result);
    }

    @Test
    void testVoidExecution() {
        // 没有返回值的代码执行
        Object result = Scripts.eval("int x = 5;");
        assertNull(result);
    }
}