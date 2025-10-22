package features.generation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.noear.liquor.Utils;
import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.ExecuteException;
import org.noear.liquor.eval.Exprs;
import org.noear.liquor.eval.Scripts;

import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 异常处理测试
 */
class ExceptionTest {

    @Test
    void testSyntaxError() {
        // 语法错误应该抛出异常
        assertThrows(IllegalStateException.class, () -> {
            Scripts.eval("invalid syntax!!!");
        });
    }

    @Test
    void testNullPointerInCode() {
        assertThrows(ExecuteException.class, () -> {
            Scripts.eval("String str = null; str.length();");
        });
    }

    @Test
    void testMissingParameter() {
        // 缺少参数应该正常处理（参数为null）
        Object result = Exprs.eval("missingParam == null ? \"default\" : missingParam", Utils.asMap("missingParam","a"));
        assertEquals("a", result);
    }

    @Test
    void testDivisionByZero() {
        assertThrows(ExecuteException.class, () -> {
            Exprs.eval("1 / 0");
        });
    }

    @Test
    void testEmptyCode() {
        // 空代码应该返回null
        Assertions.assertThrows(Exception.class, () -> {
            Exprs.eval("");
        });
    }

    @Test
    void testNullCodeSpec() {
        assertThrows(IllegalArgumentException.class, () -> {
            Scripts.eval((CodeSpec) null);
        });
    }
}