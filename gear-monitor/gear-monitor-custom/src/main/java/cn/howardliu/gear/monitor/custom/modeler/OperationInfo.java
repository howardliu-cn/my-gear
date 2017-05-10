package cn.howardliu.gear.monitor.custom.modeler;

import javax.management.MBeanOperationInfo;
import java.util.Locale;

import static javax.management.MBeanOperationInfo.*;

/**
 * <br>created at 17-5-10
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class OperationInfo extends MethodInfo {
    protected String impact = "UNKNOWN";
    protected String role = "operation";

    /**
     * The "impact" of this operation, which should be a (case-insensitive)
     * string value "ACTION", "ACTION_INFO", "INFO", or "UNKNOWN".
     *
     * @return "ACTION", "ACTION_INFO", "INFO", or "UNKNOWN"
     */
    public String getImpact() {
        return impact;
    }

    public OperationInfo setImpact(String impact) {
        if (impact == null) {
            this.impact = null;
        } else {
            this.impact = impact.toUpperCase(Locale.ENGLISH);
        }
        return this;
    }

    /**
     * The role of this operation string value.
     *
     * @return "getter", "setter", "operation", or "constructor"
     */
    public String getRole() {
        return role;
    }

    public OperationInfo setRole(String role) {
        this.role = role;
        return this;
    }

    public String getReturnType() {
        if (type == null) {
            return "void";
        }
        return type;
    }

    public OperationInfo setReturnType(String returnType) {
        this.type = returnType;
        return this;
    }

    public MBeanOperationInfo getInfo() {
        if (info == null) {
            int impact = UNKNOWN;
            switch (getImpact()) {
                case "ACTION": {
                    impact = ACTION;
                    break;
                }
                case "ACTION_INFO": {
                    impact = ACTION_INFO;
                    break;
                }
                case "INFO": {
                    impact = INFO;
                    break;
                }
            }
            info = new MBeanOperationInfo(
                    getName(),
                    getDescription(),
                    getMBeanParameterInfos(),
                    getReturnType(),
                    impact
            );
        }
        return (MBeanOperationInfo) info;
    }
}
