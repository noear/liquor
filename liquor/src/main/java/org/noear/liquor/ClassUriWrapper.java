package org.noear.liquor;

import java.net.URI;

/**
 * This code comes from: Arthas project
 * */
public class ClassUriWrapper {
    private final URI uri;

    private final String className;

    public ClassUriWrapper(String className, URI uri) {
        this.className = className;
        this.uri = uri;
    }

    public URI getUri() {
        return uri;
    }

    public String getClassName() {
        return className;
    }
}
