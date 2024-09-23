package org.noear.liquor;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * This code mainly from: Arthas project
 * */
public class MemoryByteCode extends SimpleJavaFileObject {
    private static final char PKG_SEPARATOR = '.';
    private static final char DIR_SEPARATOR = '/';
    private static final String CLASS_FILE_SUFFIX = ".class";

    private ByteArrayOutputStream outputStream;
    protected boolean defined;

    public MemoryByteCode(String className) {
        super(URI.create("byte:///" + className.replace(PKG_SEPARATOR, DIR_SEPARATOR)
                + Kind.CLASS.extension), Kind.CLASS);
    }

    @Override
    public OutputStream openOutputStream() {
        if (outputStream == null) {
            outputStream = new ByteArrayOutputStream();
        }
        return outputStream;
    }

    public byte[] getByteCode() {
        return outputStream.toByteArray();
    }

    /**
     * 移除字节码（以减少内存副本）
     */
    protected void delByteCode() {
        outputStream = null;
    }

    private String className;

    public String getClassName() {
        if (className == null) {
            //缓存，减少计算
            className = getName();
            className = className.replace(DIR_SEPARATOR, PKG_SEPARATOR);
            className = className.substring(1, className.indexOf(CLASS_FILE_SUFFIX));
        }

        return className;
    }
}