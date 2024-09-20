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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 代码申明
 *
 * @author noear
 * @since 1.2
 */
public class CodeSpec {
    private final String code;
    private Class<?>[] imports;
    private Map<String, Class<?>> parameters;
    private Class<?> returnType;

    public CodeSpec(String code) {
        this.code = code;
    }

    /**
     * 配置参数类型
     */
    public CodeSpec parameters(Map<String, Class<?>> parameters) {
        this.parameters = parameters;
        return this;
    }

    /**
     * 配置导入
     */
    public CodeSpec imports(Class<?>... imports) {
        this.imports = imports;
        return this;
    }

    /**
     * 配置参数类型
     */
    public CodeSpec parameters(String[] names, Class<?>[] types) {
        assert names != null;
        assert types != null;

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
     * 获取导入
     */
    public Class<?>[] getImports() {
        return imports;
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

    //////////////////

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CodeSpec)) return false;
        CodeSpec codeSpec = (CodeSpec) o;
        return Objects.equals(code, codeSpec.code) && Objects.equals(parameters, codeSpec.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, parameters);
    }
}