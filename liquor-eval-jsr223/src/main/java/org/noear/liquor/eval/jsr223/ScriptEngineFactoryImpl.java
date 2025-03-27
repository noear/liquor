package org.noear.liquor.eval.jsr223;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.util.Collections;
import java.util.List;

/**
 * @author noear
 */
public class ScriptEngineFactoryImpl implements ScriptEngineFactory {
    @Override
    public String getEngineName() {
        return "Java";
    }

    @Override
    public String getEngineVersion() {
        return "1.5.0";
    }

    @Override
    public List<String> getExtensions() {
        return Collections.emptyList();
    }

    @Override
    public List<String> getMimeTypes() {
        return Collections.emptyList();
    }

    @Override
    public List<String> getNames() {
        return Collections.emptyList();
    }

    @Override
    public String getLanguageName() {
        return "Java";
    }

    @Override
    public String getLanguageVersion() {
        return "";
    }

    @Override
    public Object getParameter(String key) {
        return null;
    }

    @Override
    public String getMethodCallSyntax(String obj, String m, String... args) {
        return "";
    }

    @Override
    public String getOutputStatement(String toDisplay) {
        return "";
    }

    @Override
    public String getProgram(String... statements) {
        return "";
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return null;
    }
}
