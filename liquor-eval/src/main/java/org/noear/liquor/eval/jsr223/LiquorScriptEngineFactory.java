/*
 * Copyright 2024 - 2025 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.noear.liquor.eval.jsr223;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Liquor 脚本引擎工厂（JSR223 适配）
 *
 * @author noear
 * @since 1.5
 */
public class LiquorScriptEngineFactory implements ScriptEngineFactory {
    private static final List<String> names = immutableList("liquor", "Liquor", "java", "Java");
    private static final List<String> mimeTypes = immutableList("text/x-java-source");
    private static final List<String> extensions = immutableList("java");


    @Override
    public String getEngineName() {
        return (String) this.getParameter("javax.script.engine");
    }

    @Override
    public String getEngineVersion() {
        return (String) this.getParameter("javax.script.engine_version");
    }

    @Override
    public List<String> getExtensions() {
        return extensions;
    }

    @Override
    public List<String> getMimeTypes() {
        return mimeTypes;
    }

    @Override
    public List<String> getNames() {
        return names;
    }

    @Override
    public String getLanguageName() {
        return (String) this.getParameter("javax.script.language");
    }

    @Override
    public String getLanguageVersion() {
        return (String) this.getParameter("javax.script.language_version");
    }

    @Override
    public Object getParameter(String key) {
        switch (key) {
            case "javax.script.name":
                return "java";
            case "javax.script.engine":
                return "Noear Liquor";
            case "javax.script.engine_version":
                return "1.6.1";
            case "javax.script.language":
                return "java";
            case "javax.script.language_version":
                return "Java - " + System.getProperty("java.specification.version");
            case "THREADING":
                return null;
            default:
                return null;
        }
    }

    @Override
    public String getMethodCallSyntax(String obj, String method, String... args) {
        StringBuilder sb = (new StringBuilder()).append(obj).append('.').append(method).append('(');
        int len = args.length;
        if (len > 0) {
            sb.append(args[0]);
        }

        for (int i = 1; i < len; ++i) {
            sb.append(',').append(args[i]);
        }

        sb.append(')');
        return sb.toString();
    }

    @Override
    public String getOutputStatement(String toDisplay) {
        return "System.out.println(" + toDisplay + ")";
    }

    @Override
    public String getProgram(String... statements) {
        StringBuilder sb = new StringBuilder();

        for (String statement : statements) {
            sb.append(statement).append(';');
        }

        return sb.toString();
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return new LiquorScriptEngine(this);
    }

    private static List<String> immutableList(String... elements) {
        return Collections.unmodifiableList(Arrays.asList(elements));
    }
}