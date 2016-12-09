package cn.howardliu.gear.commons;

import org.apache.commons.lang3.Validate;

/**
 * <br>created at 16-3-18
 *
 * @author liuxh
 * @since 1.0.0
 */
public class NameValuePair<V> {
    private final String name;
    private final V value;

    public NameValuePair(String name, V value) {
        this.name = Validate.notBlank(name, "name不能为空");
        if (value == null) {
            throw new NullPointerException("value不能为空");
        }
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "NameValuePair{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
