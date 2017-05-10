package cn.howardliu.gear.monitor.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;

/**
 * <br>created at 17-5-4
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class Probes {
    private static final Logger logger = LoggerFactory.getLogger(Probes.class);

    public static long getLong(Method method, OperatingSystemMXBean osMxBean) {
        return getNumber(method, osMxBean).longValue();
    }

    public static double getDouble(Method method, OperatingSystemMXBean osMxBean) {
        return getNumber(method, osMxBean).doubleValue();
    }

    private static Number getNumber(Method method, OperatingSystemMXBean osMxBean) {
        if (method == null) {
            return -1;
        }
        try {
            return (Number) method.invoke(osMxBean);
        } catch (Exception e) {
            if(logger.isDebugEnabled()) {
                logger.debug("error reading data from operating system mxBean", e);
            }
            return -1;
        }
    }
}
