package org.noear.liquor.eval;

import java.lang.reflect.InvocationTargetException;

/**
 * 脚本工具类
 *
 * @author noear
 * @since 1.3
 */
public interface Scripts {
    /**
     * 编译
     *
     * @param code 代码
     */
    static IExecutable compile(String code) {
        return ScriptEvaluator.getInstance().compile(code);
    }

    /**
     * 编译
     *
     * @param codeSpec 代码申明
     */
    static IExecutable compile(CodeSpec codeSpec) {
        return ScriptEvaluator.getInstance().compile(codeSpec);
    }


    /**
     * 评估
     *
     * @param code 代码
     */
    static Object eval(String code) throws InvocationTargetException {
        assert code != null;
        return eval(new CodeSpec(code));
    }

    /**
     * 评估
     *
     * @param codeSpec 代码申明
     * @param args     执行参数
     */
    static Object eval(CodeSpec codeSpec, Object... args) throws InvocationTargetException {
        return ScriptEvaluator.getInstance().eval(codeSpec, args);
    }
}
