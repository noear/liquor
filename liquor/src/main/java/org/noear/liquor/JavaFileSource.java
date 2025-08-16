package org.noear.liquor;

import javax.tools.SimpleJavaFileObject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * java 文件源码
 *
 * @author noear
 * @since 1.6
 */
public class JavaFileSource extends SimpleJavaFileObject {
    private final File fileSource;

    public JavaFileSource(File fileSource) {
        super(fileSource.toURI(), Kind.SOURCE);
        this.fileSource = fileSource;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return new String(Files.readAllBytes(fileSource.toPath()), StandardCharsets.UTF_8);
    }
}