package org.noear.liquor.eval.jsr223;

import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.Scripts;

import javax.script.*;
import java.io.BufferedReader;
import java.io.Reader;

/**
 * @author noear 2024/9/23 created
 */
public class LiquorScriptEngine extends AbstractScriptEngine {
    private LiquorScriptEngineFactory factory;

    public LiquorScriptEngine(LiquorScriptEngineFactory factory) {
        this.factory = factory;
    }

    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        Bindings bindings = new SimpleBindings();

        //先加载全局的
        bindings.putAll(this.getBindings(ScriptContext.GLOBAL_SCOPE));
        if (context != null) {
            bindings.putAll(context.getBindings(ScriptContext.GLOBAL_SCOPE));
        }

        //再加载引擎的
        bindings.putAll(this.getBindings(ScriptContext.ENGINE_SCOPE));
        if (context != null) {
            bindings.putAll(context.getBindings(ScriptContext.ENGINE_SCOPE));

        }

        try {
            CodeSpec codeSpec = new CodeSpec(script);

            //如果有返回，则申明返回类型为 object
            if (script.contains("return ")) {
                codeSpec.returnType(Object.class);
            }

            if (bindings.isEmpty()) {
                return Scripts.eval(codeSpec);
            } else {
                return Scripts.eval(codeSpec.parameters(bindings), bindings);
            }
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        StringBuilder buf = new StringBuilder();
        try (BufferedReader reader1 = new BufferedReader(reader)) {
            buf.append(reader1.readLine());
        } catch (Exception e) {
            throw new ScriptException(e);
        }

        return eval(buf.toString(), context);
    }

    @Override
    public Bindings createBindings() {
        return new SimpleBindings();
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return factory;
    }
}