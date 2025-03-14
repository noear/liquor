package org.noear.liquor;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.*;

/**
 * This code mainly from: Arthas project
 *
 * @author noear
 * @since 1.3.12
 * */
public class DynamicCompilerException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private List<Diagnostic<? extends JavaFileObject>> diagnostics;

    public DynamicCompilerException(String message, List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        super(message);
        this.diagnostics = diagnostics;
    }

    public DynamicCompilerException(Throwable cause, List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        super(cause);
        this.diagnostics = diagnostics;
    }

    private String getErrors() {
        StringBuilder buf = new StringBuilder();

        if (diagnostics != null) {
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
                String packageName = getPackageName(diagnostic);

                if (packageName == null) {
                    buf.append(diagnostic.toString()).append("\n");
                } else {
                    buf.append("/").append(packageName).append(".").append(diagnostic.toString().substring(1)).append("\n");
                }
            }
        }

        if (buf.length() > 0) {
            buf.setLength(buf.length() - 1);
        }

        return buf.toString();
    }

    private String getPackageName(Diagnostic<? extends JavaFileObject> diagnostic) {
        try {
            String source = diagnostic.getSource().getCharContent(true).toString();
            if (source.startsWith("package ")) {
                int end = source.indexOf(";");
                return source.substring("package ".length(), end);
            }
        } catch (Exception skip) {

        }

        return null;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + ":\n\n" + getErrors();
    }
}