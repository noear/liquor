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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

/**
 * 脚本评估器
 *
 * @author noear
 * @since 1.2
 */
public class ScriptEvaluator extends AbstractEvaluator implements IEvaluator {
    @Override
    protected Class<?> build(CodeSpec codeSpec) {
        //1.分离导入代码

        StringBuilder importBuilder = new StringBuilder();
        StringBuilder codeBuilder = new StringBuilder();

        if (codeSpec.getCode().contains("import ")) {
            BufferedReader reader = new BufferedReader(new StringReader(codeSpec.getCode()));

            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().startsWith("import ")) {
                        importBuilder.append(line).append("\n");
                    } else {
                        codeBuilder.append(line).append("\n");
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        } else {
            codeBuilder.append(codeSpec.getCode());
        }


        //2.构建代码申明

        String clazzName = "Script$" + codeSpec.getCodeKey();

        StringBuilder code = new StringBuilder();

        if (importBuilder.length() > 0) {
            code.append(importBuilder).append("\n");
        }

        code.append("public class ").append(clazzName).append(" {\n");
        {
            code.append("  public static ");
            if (codeSpec.getReturnType() != null) {
                code.append(codeSpec.getReturnType().getName());
            } else {
                code.append("void");
            }
            code.append(" _main$(");

            if (codeSpec.getParameters() != null && codeSpec.getParameters().size() > 0) {
                for (Map.Entry<String, Class<?>> kv : codeSpec.getParameters().entrySet()) {
                    code.append(kv.getValue().getName()).append(" ").append(kv.getKey()).append(",");
                }
                code.setLength(code.length() - 1);
            }
            code.append(")\n");
            code.append("  {\n");
            code.append("    ").append(codeBuilder).append("\n");
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
}