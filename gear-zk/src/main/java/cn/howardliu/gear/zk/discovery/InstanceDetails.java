package cn.howardliu.gear.zk.discovery;

import org.codehaus.jackson.map.annotate.JsonRootName;

/**
 * <br/>created at 16-5-5
 *
 * @author liuxh
 * @since 1.0.0
 */
@JsonRootName("details")
public class InstanceDetails {
    // TODO 需要补充
    private String description;

    public InstanceDetails() {
        this("");
    }

    public InstanceDetails(String description) {
        this.description = description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
