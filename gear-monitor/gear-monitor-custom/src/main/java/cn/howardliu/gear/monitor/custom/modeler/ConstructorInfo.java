package cn.howardliu.gear.monitor.custom.modeler;

import javax.management.MBeanConstructorInfo;

/**
 * <br>created at 17-5-10
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConstructorInfo extends MethodInfo {
    public MBeanConstructorInfo getInfo() {
        if (info == null) {
            info = new MBeanConstructorInfo(getName(), getDescription(), getMBeanParameterInfos());
        }
        return (MBeanConstructorInfo) info;
    }
}
