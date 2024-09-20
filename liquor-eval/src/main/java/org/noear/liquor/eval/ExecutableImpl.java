package org.noear.liquor.eval;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;

/**
 * 可执行的
 *
 * @author noear
 * @since 1.2
 */
public class ExecutableImpl implements IExecutable{
    private final Class<?> clazz;
    private final CodeSpec codeSpec;

    public ExecutableImpl(Class<?> clazz, CodeSpec codeSpec) {
        this.clazz = clazz;
        this.codeSpec = codeSpec;
    }

    /**
     * 获取类
     */
    public Class<?> getClazz() {
        return clazz;
    }

    /**
     * 获取参数
     */
    public Map<String, Class<?>> getParameters() {
        if (codeSpec.getParameters() == null) {
            return null;
        }

        return Collections.unmodifiableMap(codeSpec.getParameters());
    }

    /**
     * 获取返回类型
     */
    public Class<?> getReturnType() {
        return codeSpec.getReturnType();
    }

    /**
     * 执行
     */
    public Object exec(Object... args) throws InvocationTargetException {
        try {
            return getClazz().getMethods()[0].invoke(null, args);
        } catch (InvocationTargetException e) {
            throw e;
        } catch (Exception e) {
            throw new InvocationTargetException(e);
        }
    }
}