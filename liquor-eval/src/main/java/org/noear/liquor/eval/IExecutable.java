package org.noear.liquor.eval;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 可执行的
 *
 * @author noear
 * @since 1.2
 */
public interface IExecutable {
    /**
     * 获取方法
     */
    Method getMethod();

    /**
     * 执行
     */
    Object exec(Object... args) throws InvocationTargetException;
}