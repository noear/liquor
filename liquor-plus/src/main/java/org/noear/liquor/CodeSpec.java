package org.noear.liquor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author noear
 * @since 1.1
 */
public class CodeSpec {
    private final String code;
    private final String codeKey;
    private Map<String, Class<?>> parameters;

    public CodeSpec(String code) {
        this.code = code;
        this.codeKey = generateMD5(code);
    }

    public CodeSpec parameters(Map<String, Class<?>> parameters) {
        this.parameters = parameters;
        return this;
    }

    public CodeSpec parameters(String[] names, Class<?>[] types) {
        assert names.length == types.length;

        this.parameters = new LinkedHashMap<>();
        for (int i = 0; i < names.length; i++) {
            this.parameters.put(names[i], types[i]);
        }
        return this;
    }

    public String getCode() {
        return code;
    }

    public Map<String, Class<?>> getParameters() {
        return parameters;
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
