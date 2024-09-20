package org.noear.liquor;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This code comes from: Arthas project
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
        return super.defineClass(byteCode.getClassName(), byteCode.getByteCode(), 0, byteCode.getByteCode().length);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        MemoryByteCode byteCode = byteCodes.get(name);
        if (byteCode == null) {
            return super.findClass(name);
        }

        return defineClass(byteCode);
    }

    protected void prepareClasses() {
        for (MemoryByteCode byteCode : byteCodes.values()) {
            if (byteCode.defined == false) {
                defineClass(byteCode);
            }
        }
    }

    //================

    public Map<String, Class<?>> getClasses() throws ClassNotFoundException {
        Map<String, Class<?>> classes = new HashMap<>();
        for (MemoryByteCode byteCode : byteCodes.values()) {
            classes.put(byteCode.getClassName(), loadClass(byteCode.getClassName()));
        }
        return classes;
    }

    public Map<String, MemoryByteCode> getByteCodes() {
        return Collections.unmodifiableMap(byteCodes);
    }

    public int size() {
        return byteCodes.size();
    }
}