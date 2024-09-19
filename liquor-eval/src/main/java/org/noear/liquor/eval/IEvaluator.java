package org.noear.liquor.eval;

import java.lang.reflect.InvocationTargetException;

/**
 * 评估器
 *
 * @author noear
 * @since 1.0
 */
public interface IEvaluator {
    /**
     * 设置父类加载器
     */
    void setParentClassLoader(ClassLoader parentClassLoader);

    /**
     * 设置可缓存的（默认为 true）
     */
    void setCacheable(boolean cacheable);

    /**
     * 获取类
     */
    default Class<?> getClazz(String code) {
        return getClazz(new CodeSpec(code));
    }

    /**
     * 获取类
     */
    Class<?> getClazz(CodeSpec codeSpec);

    /**
     * 评估
     *
     * @param code 代码
     */
    default Object evaluate(String code) throws InvocationTargetException {
        return evaluate(new CodeSpec(code));
    }

    /**
     * 评估
     *
     * @param codeSpec 代码申明
     */
    default Object evaluate(CodeSpec codeSpec) throws InvocationTargetException {
        return evaluate(codeSpec, new Object[0]);
    }

    /**
     * 评估
     *
     * @param codeSpec 代码申明
     * @param args     执行参数
     */
    Object evaluate(CodeSpec codeSpec, Object[] args) throws InvocationTargetException;
}