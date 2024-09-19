package org.noear.liquor.eval;

import org.noear.liquor.DynamicCompiler;

import java.util.Map;

/**
 * 脚本评估器
 *
 * @author noear
 * @since 1.1
 */
public class ScriptEvaluator extends AbstractEvaluator implements IEvaluator {
    @Override
    protected Class<?> build(CodeSpec codeSpec) {
        String clazzName = "Script$" + codeSpec.getCodeKey();

        StringBuilder code = new StringBuilder();
        code.append("public class ").append(clazzName).append(" {\n");
        {
            code.append("  public static ");
            if (codeSpec.getReturnType() != null) {
                code.append(codeSpec.getReturnType().getName());
            } else {
                code.append("void");
            }
            code.append(" main(");

            if (codeSpec.getParameters() != null && codeSpec.getParameters().size() > 0) {
                for (Map.Entry<String, Class<?>> kv : codeSpec.getParameters().entrySet()) {
                    code.append(kv.getValue().getName()).append(" ").append(kv.getKey()).append(",");
                }
                code.setLength(code.length() - 1);
            }
            code.append(")\n");
            code.append("  {\n");
            code.append("    ").append(codeSpec.getCode()).append("\n");
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