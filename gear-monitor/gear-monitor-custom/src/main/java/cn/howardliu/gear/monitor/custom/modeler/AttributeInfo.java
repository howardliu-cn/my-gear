package cn.howardliu.gear.monitor.custom.modeler;

import cn.howardliu.gear.commons.utils.IntrospectionUtils;

import javax.management.MBeanAttributeInfo;

/**
 * <br>created at 17-5-10
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class AttributeInfo extends FeatureInfo {
    protected String displayName;
    protected String getMethod;
    protected String setMethod;
    protected boolean readable = true;
    protected boolean writeable = true;
    protected boolean isIs = false;

    public String getDisplayName() {
        return displayName;
    }

    public AttributeInfo setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getGetMethod() {
        if (getMethod == null) {
            getMethod = getMethodName(getName(), true, isIs());
        }
        return getMethod;
    }

    public AttributeInfo setGetMethod(String getMethod) {
        this.getMethod = getMethod;
        return this;
    }

    public String getSetMethod() {
        if (setMethod == null) {
            setMethod = getMethodName(getName(), false, false);
        }
        return setMethod;
    }

    public AttributeInfo setSetMethod(String setMethod) {
        this.setMethod = setMethod;
        return this;
    }

    public boolean isIs() {
        return isIs;
    }

    public AttributeInfo setIs(boolean is) {
        isIs = is;
        return this;
    }

    public boolean isReadable() {
        return readable;
    }

    public AttributeInfo setReadable(boolean readable) {
        this.readable = readable;
        return this;
    }

    public boolean isWriteable() {
        return writeable;
    }

    public AttributeInfo setWriteable(boolean writeable) {
        this.writeable = writeable;
        return this;
    }

    public MBeanAttributeInfo getInfo() {
        if (info == null) {
            info = new MBeanAttributeInfo(
                    getName(),
                    getType(),
                    getDescription(),
                    isReadable(),
                    isWriteable(),
                    isIs()
            );
        }
        return (MBeanAttributeInfo) info;
    }

    private String getMethodName(String name, boolean getter, boolean isIs) {
        String pre;
        if (getter) {
            if (isIs) {
                pre = "is";
            } else {
                pre = "get";
            }
        } else {
            pre = "set";
        }
        return pre + IntrospectionUtils.capitalize(name);
    }
}
