package org.noear.liquor.eval;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 表达式快捷方法
 *
 * @author noear
 * @since 1.3
 */
public interface Express {
    /**
     * 编译
     *
     * @param code 代码
     */
    static IExecutable compile(String code) {
        return ExpressionEvaluator.getInstance().compile(code);
    }

    /**
     * 编译
     *
     * @param codeSpec 代码申明
     */
    static IExecutable compile(CodeSpec codeSpec) {
        return ExpressionEvaluator.getInstance().compile(codeSpec);
    }


    /**
     * 评估
     *
     * @param code 代码
     */
    static Object eval(String code) throws InvocationTargetException {
        return ExpressionEvaluator.getInstance().eval(code);
    }

    /**
     * 评估
     *
     * @param codeSpec 代码申明
     * @param args     执行参数
     */
    static Object eval(CodeSpec codeSpec, Object... args) throws InvocationTargetException {
        return ExpressionEvaluator.getInstance().eval(codeSpec, args);
    }

    /**
     * 评估
     *
     * @param code 代码
     */
    static Object eval(String code, Map<String, Object> context) throws InvocationTargetException {
        assert context != null;

        ParamSpec[] parameters = new ParamSpec[context.size()];
        Object[] args = new Object[context.size()];

        int idx = 0;
        for (Map.Entry<String, Object> entry : context.entrySet()) {
            parameters[idx] = new ParamSpec(entry.getKey(), entry.getValue().getClass());
            args[idx] = entry.getValue();
        }

        return eval(new CodeSpec(code).parameters(parameters), args);
    }
}