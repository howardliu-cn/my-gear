package cn.howardliu.gear.monitor.custom.modeler;

import javax.management.MBeanParameterInfo;

/**
 * <br>created at 17-5-10
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class ParameterInfo extends FeatureInfo {
    public MBeanParameterInfo getInfo() {
        if (info == null) {
            info = new MBeanParameterInfo(getName(), getType(), getDescription());
        }
        return null;
    }
}
