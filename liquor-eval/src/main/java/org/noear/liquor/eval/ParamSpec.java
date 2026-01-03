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

import org.noear.liquor.Utils;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * 参数申明
 *
 * @author noear
 * @since 1.2
 * @since 1.5
 */
public class ParamSpec implements Comparable<ParamSpec> {
    private String name;
    private Class<?> type;
    private String typeName;

    public ParamSpec(String name, Class<?> type) {
        this.name = name;
        this.type = type;
        this.typeName = Utils.getTypeName(resolveParamType(type));
    }

    public ParamSpec(String name, String typeName) {
        this.name = name;
        this.type = null;
        this.typeName = typeName;
    }

    private static Class<?> resolveParamType(Class<?> type) {

        if (Modifier.isPublic(type.getModifiers())) {
            // 如果是公有的不变
            return type;
        } else {
            // 否则转换为相关接口

            // 1. Map 接口 (新增)
            if (Map.class.isAssignableFrom(type)) {
                return Map.class;
            }

            // 2. Collection 接口。优先检查更具体的接口
            if (List.class.isAssignableFrom(type)) {
                return List.class;
            } else if (Set.class.isAssignableFrom(type)) {
                return Set.class;
            } else if (Queue.class.isAssignableFrom(type)) {
                return Queue.class;
            } else if (Collection.class.isAssignableFrom(type)) {
                return Collection.class;
            } else if (Iterator.class.isAssignableFrom(type)) {
                return Iterator.class;
            } else if (Stream.class.isAssignableFrom(type)) {
                return Stream.class;
            }

            return type;
        }
    }

    /**
     * 获取参数名字
     */
    public String getName() {
        return name;
    }


    /**
     * 获取参数类型名字
     *
     * @since 1.6.4
     */
    public String getTypeName() {
        return typeName;
    }


    /**
     * 获取参数类型
     *
     * @since 1.5.8
     * @deprecated 1.6.4 {@link #getTypeName()}
     */
    @Deprecated
    public Class<?> getType() {
        return type;
    }

    /**
     * 获取参数类型
     *
     * @deprecated 1.5.8 {@link #getTypeName()}
     */
    @Deprecated
    public Class<?> getValue() {
        return type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParamSpec)) return false;
        ParamSpec paramSpec = (ParamSpec) o;
        return Objects.equals(name, paramSpec.name) && Objects.equals(typeName, paramSpec.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, typeName);
    }

    @Override
    public int compareTo(ParamSpec o) {
        return this.name.compareTo(o.name);
    }
}
