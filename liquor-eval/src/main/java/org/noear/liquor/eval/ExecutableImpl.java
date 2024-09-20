package org.noear.liquor.eval;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 可执行的
 *
 * @author noear
 * @since 1.2
 */
public class ExecutableImpl implements IExecutable {
    private final Method method;

    public ExecutableImpl(Class<?> clazz) {
        this.method = clazz.getDeclaredMethods()[0];
    }

    /**
     * 获取方法
     */
    public Method getMethod() {
        return method;
    }

    /**
     * 执行
     */
    public Object exec(Object... args) throws InvocationTargetException {
        try {
            return method.invoke(null, args);
        } catch (InvocationTargetException e) {
            throw e;
        } catch (Exception e) {
            throw new InvocationTargetException(e);
        }
    }
}