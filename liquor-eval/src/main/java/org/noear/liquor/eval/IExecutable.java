package org.noear.liquor.eval;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 可执行的
 *
 * @author noear
 * @since 1.2
 */
public interface IExecutable {
    /**
     * 获取类
     */
    Class<?> getClazz();

    /**
     * 获取方法
     */
    Method getMethod();

    /**
     * 获取参数
     */
    Map<String, Class<?>> getParameters();

    /**
     * 获取返回类型
     */
    Class<?> getReturnType();

    /**
     * 执行
     */
    Object exec(Object... args) throws InvocationTargetException;
}