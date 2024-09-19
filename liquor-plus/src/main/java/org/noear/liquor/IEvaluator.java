package org.noear.liquor;

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
     * 执行
     */
    default Object evaluate(String code) throws InvocationTargetException {
        return evaluate(new CodeSpec(code));
    }

    /**
     * 执行
     *
     * @param codeSpec 代码申明
     */
    Object evaluate(CodeSpec codeSpec) throws InvocationTargetException;

    /**
     * 执行
     *
     * @param codeSpec 代码申明
     * @param args     执行参数
     */
    Object evaluate(CodeSpec codeSpec, Object[] args) throws InvocationTargetException;
}