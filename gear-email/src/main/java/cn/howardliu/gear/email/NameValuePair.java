package cn.howardliu.gear.email;

import org.apache.commons.lang3.Validate;

/**
 * <br>created at 16-3-18
 *
 * @author liuxh
 * @since 1.1.4
 */
public class NameValuePair {
    private final String name;
    private final String value;

    public NameValuePair(String name, String value) {
        this.name = Validate.notBlank(name, "name不能为空");
        this.value = Validate.notBlank(value, "value不能为空");
    }

    public String getName() {
        return name;
    }

    public String getValue() {
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
