package org.noear.liquor;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;

/**
 * This code mainly from: Arthas project
 * */
public class JavaStringSource extends SimpleJavaFileObject {
    private final String stringSource;

    public JavaStringSource(String className, String stringSource) {
        super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.stringSource = stringSource;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return stringSource;
    }
}