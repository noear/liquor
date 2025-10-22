package features.generation;

import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.Exprs;
import org.noear.liquor.eval.ParamSpec;
import org.noear.liquor.eval.Scripts;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 类型和参数处理测试
 */
public class TypeAndParameterTest {

    public static class TestUser {
        private String name;
        private int age;

        public TestUser(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() { return name; }
        public int getAge() { return age; }
    }

    @Test
    void testCustomClassParameter() {
        TestUser user = new TestUser("Alice", 30);

        CodeSpec spec = new CodeSpec("user.getAge() > 18 ? user.getName() : \"minor\"")
                .imports(TestUser.class)
                .parameters(new ParamSpec("user", TestUser.class))
                .returnType(String.class);

        Map<String, Object> context = new HashMap<>();
        context.put("user", user);

        Object result = Exprs.eval(spec, context);
        assertEquals("Alice", result);
    }

    @Test
    void testDifferentParameterTypes() {
        Map<String, Object> context = new HashMap<>();
        context.put("intVal", 10);
        context.put("doubleVal", 10.5);
        context.put("stringVal", "test");
        context.put("boolVal", true);

        Object result = Exprs.eval("intVal + \"-\" + doubleVal + \"-\" + stringVal + \"-\" + boolVal", context);
        assertEquals("10-10.5-test-true", result);
    }

    @Test
    void testListParameter() {
        Map<String, Object> context = new HashMap<>();
        context.put("list", Arrays.asList(1, 2, 3, 4, 5));

        Object result = Exprs.eval("list.size()", context);
        assertEquals(5, result);
    }

    @Test
    void testMapParameter() {
        Map<String, Object> context = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("key1", "value1");
        data.put("key2", "value2");
        context.put("map", data);

        Object result = Exprs.eval("map.get(\"key1\")", context);
        assertEquals("value1", result);
    }

    @Test
    void testReturnTypeSpecification() {
        CodeSpec spec = new CodeSpec("Math.sqrt(16)")
                .returnType(Double.class);

        Object result = Scripts.eval(spec);
        assertEquals(4.0, result);
        assertTrue(result instanceof Double);
    }
}