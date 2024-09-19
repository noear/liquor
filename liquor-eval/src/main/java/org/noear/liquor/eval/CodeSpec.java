package org.noear.liquor.eval;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 代码申明
 *
 * @author noear
 * @since 1.2
 */
public class CodeSpec {
    private final String code;
    private final String codeKey;
    private Map<String, Class<?>> parameters;
    private Class<?> returnType;

    public CodeSpec(String code) {
        this.code = code;
        this.codeKey = generateMD5(code);
    }

    /**
     * 配置参数类型
     */
    public CodeSpec parameters(Map<String, Class<?>> parameters) {
        this.parameters = parameters;
        return this;
    }

    /**
     * 配置参数类型
     */
    public CodeSpec parameters(String[] names, Class<?>[] types) {
        assert names.length == types.length;

        this.parameters = new LinkedHashMap<>();
        for (int i = 0; i < names.length; i++) {
            this.parameters.put(names[i], types[i]);
        }
        return this;
    }

    /**
     * 配置参数类型
     */
    public CodeSpec parameters(Class<?>... types) {
        this.parameters = new LinkedHashMap<>();
        for (int i = 0; i < types.length; i++) {
            this.parameters.put("$" + i, types[i]);
        }
        return this;
    }

    /**
     * 配置返回类型
     */
    public CodeSpec returnType(Class<?> returnType) {
        this.returnType = returnType;
        return this;
    }

    //////////////////

    /**
     * 获取代码
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取参数类型
     */
    public Map<String, Class<?>> getParameters() {
        return parameters;
    }

    /**
     * 获取返回类型
     */
    public Class<?> getReturnType() {
        return returnType;
    }

    protected String getCodeKey() {
        return codeKey;
    }

    private static String generateMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}