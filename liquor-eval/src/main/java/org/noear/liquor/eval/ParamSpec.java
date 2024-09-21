package org.noear.liquor.eval;

import java.util.Map;
import java.util.Objects;

/**
 * 参数申明
 *
 * @author noear
 * @since 1.2
 */
public class ParamSpec implements Map.Entry<String,Class<?>> {
    private String name;
    private Class<?> type;

    public ParamSpec(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }


    @Override
    public String getKey() {
        return name;
    }

    @Override
    public Class<?> getValue() {
        return type;
    }

    @Override
    public Class<?> setValue(Class<?> value) {
        return this.type = value;
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
}
