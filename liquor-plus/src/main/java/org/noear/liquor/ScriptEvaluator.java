package org.noear.liquor;

import java.util.Map;

/**
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
            code.append("  public static void main(");
            if (codeSpec.getParameters() != null && codeSpec.getParameters().size() > 0) {
                for (Map.Entry<String, Class<?>> kv : codeSpec.getParameters().entrySet()) {
                    code.append(kv.getValue()).append(" ").append(kv.getKey()).append(",");
                }
                code.setLength(code.length() - 1);
            }
            code.append(")\n");
            code.append("  {\n");
            code.append("    ").append(codeSpec.getCode()).append("\n");
            code.append("  }\n");
        }
        code.append("}");

        DynamicCompiler compiler = getCompiler();
        compiler.addSource(clazzName, code.toString()).build();

        try {
            return compiler.getClassLoader().loadClass(clazzName);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}