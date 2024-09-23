package org.noear.liquor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This code mainly from: Arthas project
 * */
public class DynamicClassLoader extends ClassLoader {
    private final Map<String, MemoryByteCode> byteCodes = new HashMap<>();

    public DynamicClassLoader(ClassLoader classLoader) {
        super(classLoader);
    }

    protected void registerCompiledSource(MemoryByteCode byteCode) {
        byteCodes.put(byteCode.getClassName(), byteCode);
    }

    protected Class<?> defineClass(MemoryByteCode byteCode) {
        byteCode.defined = true;
        byte[] bytes = byteCode.getByteCode();
        return super.defineClass(byteCode.getClassName(), bytes, 0, bytes.length);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        MemoryByteCode byteCode = byteCodes.get(name);
        if (byteCode == null || byteCode.defined) {
            return super.findClass(name);
        }

        return defineClass(byteCode);
    }

    /**
     * 预处理类（完成批量定义）
     */
    protected void prepareClasses() {
        for (MemoryByteCode byteCode : byteCodes.values()) {
            if (byteCode.defined == false) {
                defineClass(byteCode);
            }
        }
    }

    //================

    /**
     * 获取类名集合
     */
    public Collection<String> getClassNames() {
        return byteCodes.keySet();
    }

    /**
     * 获取集合大小
     */
    public int size() {
        return byteCodes.size();
    }
}