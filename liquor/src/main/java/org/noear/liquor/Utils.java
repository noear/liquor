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
package org.noear.liquor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 工具
 *
 * @author noear
 * @since 1.5
 */
public class Utils {
    private static final Map<Class<?>, String> typeNameCache = new ConcurrentHashMap<>();
    /**
     * 获取类型名字
     */
    public static String getTypeName(Class<?> clz) {
        if (clz == null) {
            return null;
        }

        String cached = typeNameCache.get(clz);
        if (cached != null) {
            return cached;
        }

        String name;
        if (clz.isArray()) {
            name = getTypeName(clz.getComponentType()) + "[]";
        } else {
            name = clz.getCanonicalName();
            if (name == null) {
                name = clz.getTypeName().replace('$', '.');
            }
        }

        typeNameCache.putIfAbsent(clz, name);
        return name;
    }

    /**
     * 转为一个可变 List
     */
    public static <T> List<T> asList(T... ary) {
        if (ary == null) {
            return null;
        } else {
            List<T> list = new ArrayList<>(ary.length);
            Collections.addAll(list, ary);
            return list;
        }
    }

    /**
     * 转为一个可变 Set
     */
    public static <T> Set<T> asSet(T... ary) {
        if (ary == null) {
            return null;
        } else {
            Set<T> list = new HashSet<>(ary.length);
            Collections.addAll(list, ary);
            return list;
        }
    }


    /**
     * 转为一个可变 Map
     */
    public static Map asMap(Object... keyValues) {
        if (keyValues.length % 2 != 0) {
            throw new IllegalArgumentException("keyValues.length % 2 != 0");
        }

        Map map = new LinkedHashMap(keyValues.length / 2);
        for (int i = 0; i < keyValues.length; i += 2) {
            map.put(keyValues[i], keyValues[i + 1]);
        }

        return map;
    }
}