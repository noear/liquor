/*
 * Copyright 2024 - 2025 noear.org and authors
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

import java.util.*;

/**
 * 代码申明
 *
 * @author noear
 * @since 1.2
 */
public class CodeSpec {
    private final String code;
    private final List<String> imports = new ArrayList<>();
    private final List<ParamSpec> parameters = new ArrayList<>();
    private Class<?> returnType;
    private boolean cached = true;

    public CodeSpec(String code) {
        this.code = code;
    }

    /**
     * 申明缓存的
     */
    public CodeSpec cached(boolean cached) {
        this.cached = cached;
        return this;
    }

    /**
     * 申明导入包
     */
    public CodeSpec imports(Package... imports) {
        for (Package imp : imports) {
            this.imports.add(imp.getName() + ".*");
        }
        return this;
    }

    /**
     * 申明导入类
     */
    public CodeSpec imports(Class<?>... imports) {
        for (Class<?> imp : imports) {
            this.imports.add(imp.getCanonicalName());
        }
        return this;
    }

    /**
     * 申明导入描述（字符串形式，支持更丰富）
     */
    public CodeSpec imports(String... imports) {
        for (String imp : imports) {
            this.imports.add(imp);
        }
        return this;
    }

    /**
     * 申明参数
     *
     * @param params 参数
     */
    public CodeSpec parameters(ParamSpec... params) {
        //不需要排序，已指定顺位
        this.parameters.addAll(Arrays.asList(params));
        return this;
    }

    /**
     * 绑定
     *
     * @param params 参数（支持 [key,val] 或 [key,clz]）
     */
    public CodeSpec parameters(Map<String, Object> params) {
        assert params != null;

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() instanceof Class<?>) {
                parameters.add(new ParamSpec(entry.getKey(), (Class<?>) entry.getValue()));
            } else {
                parameters.add(new ParamSpec(entry.getKey(), entry.getValue().getClass()));
            }
        }
        //要排序下，避免 hash-map 的顺位变化
        Collections.sort(parameters);

        return this;
    }

    /**
     * 申明返回类型
     */
    public CodeSpec returnType(Class<?> returnType) {
        this.returnType = returnType;
        return this;
    }

    //////////////////

    /**
     * 是否缓存
     */
    public boolean isCached() {
        return cached;
    }

    /**
     * 获取代码申明
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取导入申明
     */
    public Collection<String> getImports() {
        return imports;
    }

    /**
     * 获取参数申明
     */
    public Collection<ParamSpec> getParameters() {
        return parameters;
    }

    /**
     * 获取返回类型申明
     */
    public Class<?> getReturnType() {
        return returnType;
    }

    /// ///////////////

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CodeSpec)) return false;
        CodeSpec codeSpec = (CodeSpec) o;
        return Objects.equals(code, codeSpec.code) && Objects.equals(parameters, codeSpec.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, parameters);
    }
}