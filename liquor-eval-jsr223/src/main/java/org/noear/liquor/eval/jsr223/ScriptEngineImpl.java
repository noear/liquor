package org.noear.liquor.eval.jsr223;

import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.Scripts;

import javax.script.*;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author noear 2024/9/23 created
 */
public class ScriptEngineImpl implements ScriptEngine {
    private ScriptContext _context = new SimpleScriptContext();

    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        Map<String, Object> bindings = new LinkedHashMap<>();
        bindings.putAll(_context.getBindings(ScriptContext.ENGINE_SCOPE));
        bindings.putAll(context.getBindings(ScriptContext.ENGINE_SCOPE));


        try {
            CodeSpec codeSpec = new CodeSpec(script);
            if (script.contains("return ")) {
                codeSpec.returnType(Object.class);
            }

            if (bindings == null || bindings.isEmpty()) {
                return Scripts.eval(codeSpec);
            } else {
                return Scripts.eval(codeSpec.parameters(bindings), bindings);
            }
        } catch (Exception ex) {
            throw new ScriptException(ex);
        }
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        return null;
    }

    @Override
    public Object eval(String script) throws ScriptException {
        Map<String, Object> bindings = new LinkedHashMap<>();
        bindings.putAll(_context.getBindings(ScriptContext.ENGINE_SCOPE));

        try {
            CodeSpec codeSpec = new CodeSpec(script);
            if (script.contains("return ")) {
                codeSpec.returnType(Object.class);
            }

            if (bindings == null || bindings.isEmpty()) {
                return Scripts.eval(codeSpec);
            } else {
                return Scripts.eval(codeSpec.parameters(bindings), bindings);
            }
        } catch (Exception ex) {
            throw new ScriptException(ex);
        }
    }

    @Override
    public Object eval(Reader reader) throws ScriptException {
        return null;
    }

    @Override
    public Object eval(String script, Bindings n) throws ScriptException {
        return null;
    }

    @Override
    public Object eval(Reader reader, Bindings n) throws ScriptException {
        return null;
    }

    @Override
    public void put(String key, Object value) {
        _context.setAttribute(key, value, ScriptContext.ENGINE_SCOPE);
    }

    @Override
    public Object get(String key) {
        return _context.getAttribute(key);
    }

    @Override
    public Bindings getBindings(int scope) {
        return _context.getBindings(scope);
    }

    @Override
    public void setBindings(Bindings bindings, int scope) {
        _context.setBindings(bindings, scope);
    }

    @Override
    public Bindings createBindings() {
        return new SimpleBindings();
    }

    @Override
    public ScriptContext getContext() {
        return this._context;
    }

    @Override
    public void setContext(ScriptContext context) {
        assert context != null;

        this._context = context;
    }

    @Override
    public ScriptEngineFactoryImpl getFactory() {
        return null;
    }
}