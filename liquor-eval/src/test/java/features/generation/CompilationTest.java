package features.generation;

import org.junit.jupiter.api.Test;
import org.noear.liquor.eval.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 编译和缓存功能测试
 */
class CompilationTest {

    @Test
    void testBatchCompile() {
        CodeSpec spec1 = new CodeSpec("return x + y;")
                .parameters(new ParamSpec("x", Integer.class))
                .parameters(new ParamSpec("y", Integer.class))
                .returnType(Object.class);

        CodeSpec spec2 = new CodeSpec("return x * y;")
                .parameters(new ParamSpec("x", Integer.class))
                .parameters(new ParamSpec("y", Integer.class))
                .returnType(Object.class);

        List<CodeSpec> specs = Arrays.asList(spec1, spec2);
        Map<CodeSpec, Execable> compiled = Scripts.compile(specs);

        assertEquals(2, compiled.size());
        assertTrue(compiled.containsKey(spec1));
        assertTrue(compiled.containsKey(spec2));
    }

    @Test
    void testCachedExecution() {
        CodeSpec spec = new CodeSpec("value * 2")
                .parameters(new ParamSpec("value", Integer.class))
                .cached(true);
        Execable execable1 = Exprs.compile(spec);
        Execable execable2 = Exprs.compile(spec);

        // 应该返回相同的实例（缓存）
        assertSame(execable1, execable2);
    }

    @Test
    void testNonCachedExecution() {
        CodeSpec spec = new CodeSpec("value * 2")
                .parameters(new ParamSpec("value", Integer.class))
                .cached(false);
        Execable execable1 = Exprs.compile(spec);
        Execable execable2 = Exprs.compile(spec);

        // 应该返回不同的实例（不缓存）
        assertNotSame(execable1, execable2);
    }

    @Test
    void testSameCodeDifferentSpecs() {
        CodeSpec spec1 = new CodeSpec("a + b")
                .parameters(new ParamSpec("a", Integer.class))
                .parameters(new ParamSpec("b", Integer.class))
                .returnType(Object.class)
                .cached(false);

        CodeSpec spec2 = new CodeSpec("a + b")
                .parameters(new ParamSpec("a", Integer.class))
                .parameters(new ParamSpec("b", Integer.class))
                .returnType(Object.class)
                .cached(false);

        Execable execable1 = Exprs.compile(spec1);
        Execable execable2 = Exprs.compile(spec2);

        // 相同的代码，不同的spec实例，应该编译为不同的执行器
        assertNotSame(execable1, execable2);
    }
}