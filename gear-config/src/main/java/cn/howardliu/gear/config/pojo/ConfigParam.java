package cn.howardliu.gear.config.pojo;

import java.io.Serializable;

/**
 * <br/>created at 16-5-7
 *
 * @author liuxh
 * @since 1.0.1
 */
public class ConfigParam implements Serializable {
    private Long sid;
    private String name;
    private String type;
    private String value;

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
