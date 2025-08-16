package org.noear.liquor;

import javax.tools.SimpleJavaFileObject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
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
    private final Charset charset;

    public JavaFileSource(File fileSource) {
        this(fileSource, null);
    }

    public JavaFileSource(File fileSource, Charset charset) {
        super(fileSource.toURI(), Kind.SOURCE);
        this.fileSource = fileSource;

        if (charset == null) {
            this.charset = StandardCharsets.UTF_8;
        } else {
            this.charset = charset;
        }
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return new String(Files.readAllBytes(fileSource.toPath()), charset);
    }
}