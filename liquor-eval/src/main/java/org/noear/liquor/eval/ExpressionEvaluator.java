/*
 * Copyright 2024 noear.org and authors
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
package org.noear.liquor.eval;

import org.noear.liquor.DynamicCompiler;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 表达式评估器（只能写一行代码）
 *
 * @author noear
 * @since 1.2
 */
public class ExpressionEvaluator extends AbstractEvaluator implements IEvaluator {

    @Override
    protected Class<?> build(CodeSpec codeSpec) {
        String clazzName = "Expression$" + codeSpec.getCodeKey();

        StringBuilder code = new StringBuilder();
        code.append("public class ").append(clazzName).append(" {\n");
        {
            code.append("  public static Object main(");
            if (codeSpec.getParameters() != null && codeSpec.getParameters().size() > 0) {
                for (Map.Entry<String, Class<?>> kv : codeSpec.getParameters().entrySet()) {
                    code.append(kv.getValue().getName()).append(" ").append(kv.getKey()).append(",");
                }
                code.setLength(code.length() - 1);
            }
            code.append(")\n");
            code.append("  {\n");
            code.append("    return ").append(codeSpec.getCode()).append(";\n");
            code.append("  }\n");
        }
        code.append("}");

        if (printable) {
            System.out.println(code.toString());
        }

        DynamicCompiler compiler = getCompiler();
        compiler.addSource(clazzName, code.toString()).build();

        try {
            return compiler.getClassLoader().loadClass(clazzName);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }


    /**
     * 评估
     *
     * @param code 代码
     */
    public Object evaluate(String code, Map<String, Object> bindings) throws InvocationTargetException {
        String[] argsNames = new String[bindings.size()];
        Class[] argsTypes = new Class[bindings.size()];
        Object[] args = new Object[bindings.size()];

        int idx = 0;
        for (Map.Entry<String, Object> entry : bindings.entrySet()) {
            argsNames[idx] = entry.getKey();
            argsTypes[idx] = entry.getValue().getClass();
            args[idx] = entry.getValue();
        }

        return evaluate(new CodeSpec(code).parameters(argsNames, argsTypes), args);
    }
}