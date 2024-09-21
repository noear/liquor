package org.noear.liquor.eval;

/**
 * 参数申明
 *
 * @author noear
 * @since 1.2
 */
public class ParamSpec {
    private String name;
    private Class<?> type;

    public ParamSpec(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }
}
