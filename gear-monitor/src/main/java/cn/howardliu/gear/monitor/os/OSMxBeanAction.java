package cn.howardliu.gear.monitor.os;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * <br>created at 17-5-4
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class OSMxBeanAction {
    private static final Logger logger = LoggerFactory.getLogger(OSMxBeanAction.class);

    public static int getAvailableProcessors() {
        return ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
    }

    public static long getProcessCpuTime() {
        return getLongFromOperationSystemMXBean(ManagementFactory.getOperatingSystemMXBean(), "getProcessCpuTime");
    }

    private static Long getLongFromOperationSystemMXBean(OperatingSystemMXBean mxBean, String methodName) {
        try {
            Optional<?> result = getFromOperationSystemMXBean(mxBean, methodName);
            if (result.isPresent()) {
                return (Long) result.get();
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage(), e);
        }
        return -1L;
    }

    private static Optional<?> getFromOperationSystemMXBean(OperatingSystemMXBean mxBean, String methodName)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return Optional.ofNullable(mxBean.getClass().getMethod(methodName).invoke(mxBean, methodName));
    }
}
