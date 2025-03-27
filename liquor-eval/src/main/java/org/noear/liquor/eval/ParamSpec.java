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

import java.util.Objects;

/**
 * 参数申明
 *
 * @author noear
 * @since 1.2
 * @since 1.5
 */
public class ParamSpec  implements Comparable<ParamSpec> {
    private String name;
    private Class<?> type;

    public ParamSpec(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<?> getValue() {
        return type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParamSpec)) return false;
        ParamSpec paramSpec = (ParamSpec) o;
        return Objects.equals(name, paramSpec.name) && Objects.equals(type, paramSpec.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public int compareTo(ParamSpec o) {
        return this.name.compareTo(o.name);
    }
}
