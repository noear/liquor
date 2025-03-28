/*
 * Copyright 2025 noear.org and authors
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

import org.noear.liquor.eval.CodeSpec;
import org.noear.liquor.eval.Scripts;

import javax.script.*;
import java.io.BufferedReader;
import java.io.Reader;

/**
 * Liquor 脚本引擎（JSR223 适配）
 *
 * @author noear
 * @since 1.5
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
        String line;

        try (BufferedReader reader1 = new BufferedReader(reader)) {
            while ((line = reader1.readLine()) != null) {
                buf.append(line).append(System.lineSeparator());
            }
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