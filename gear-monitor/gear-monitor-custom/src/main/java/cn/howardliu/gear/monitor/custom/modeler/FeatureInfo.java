package cn.howardliu.gear.monitor.custom.modeler;

import javax.management.MBeanFeatureInfo;
import java.io.Serializable;

/**
 * <br>created at 17-5-10
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class FeatureInfo implements Serializable {
    protected String description;
    protected String name;
    protected MBeanFeatureInfo info = null;
    protected String type;

    public String getDescription() {
        return description;
    }

    public FeatureInfo setDescription(String description) {
        this.description = description;
        this.info = null;
        return this;
    }

    public String getName() {
        return name;
    }

    public FeatureInfo setName(String name) {
        this.name = name;
        this.info = null;
        return this;
    }

    public String getType() {
        return type;
    }

    public FeatureInfo setType(String type) {
        this.type = type;
        this.info = null;
        return this;
    }
}
