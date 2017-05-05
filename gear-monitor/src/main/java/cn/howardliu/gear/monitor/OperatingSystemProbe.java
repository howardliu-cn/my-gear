package cn.howardliu.gear.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;

/**
 * <br>created at 17-5-4
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class OperatingSystemProbe {
    protected static final OperatingSystemMXBean osMxBean = ManagementFactory.getOperatingSystemMXBean();
    private static final Logger logger = LoggerFactory.getLogger(OperatingSystemProbe.class);

    /**
     * Returns a given method of the {@link com.sun.management.OperatingSystemMXBean}, or null if the method is not found or unavailable.
     *
     * @param methodName a given method
     * @return method or null
     */
    protected static Method getMethod(String methodName) {
        try {
            return Class.forName("com.sun.management.OperatingSystemMXBean").getMethod(methodName);
        } catch (Exception e) {
            // not available
            return null;
        }
    }

    /**
     * Returns a given method of the {@link com.sun.management.UnixOperatingSystemMXBean}, or null if the method is not found or unavailable.
     *
     * @param methodName a given method
     * @return method or null
     */
    protected static Method getUnixMethod(String methodName) {
        try {
            return Class.forName("com.sun.management.UnixOperatingSystemMXBean").getMethod(methodName);
        } catch (Exception e) {
            // not available
            return null;
        }
    }
}
