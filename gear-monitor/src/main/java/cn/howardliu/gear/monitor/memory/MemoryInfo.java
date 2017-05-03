package cn.howardliu.gear.monitor.memory;

import cn.howardliu.gear.monitor.Constants;
import cn.howardliu.gear.monitor.jvm.JvmStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * <br>created at 17-5-3
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class MemoryInfo {
    private static final Logger logger = LoggerFactory.getLogger(MemoryInfo.class);
    private JvmStats.JvmMemoryInfo jvmMemoryInfo;
    private MemoryUsage heapMemoryUsage;
    private MemoryUsage nonHeapMemoryUsage;
    private MemoryUsage permGenMemoryUsage;//
    private int loadedClassesCount;
    private long garbageCollectionTimeMillis;
    private MemorySize swapSpaceSize;//
    private MemorySize physicalSpaceSize;//
    private long committedVirtualMemorySize;//
    private List<JvmStats.MemoryPool> memPoolInfos;
    private List<JvmStats.GarbageCollector> gcInfoList;
    private List<JvmStats.BufferPool> bufferPools;

    public MemoryInfo() {
    }

    public MemoryInfo refresh() {
        JvmStats jvmStats = JvmStats.jvmStats();

        jvmMemoryInfo = jvmStats.getMem().getJvmMemoryInfo();
        permGenMemoryUsage = permGenMemoryUsage();

        heapMemoryUsage = jvmStats.getMem().getHeapMemoryUsage();
        nonHeapMemoryUsage = jvmStats.getMem().getNonHeapMemoryUsage();

        loadedClassesCount = jvmStats.getClasses().getLoadedClassCount();

        swapSpaceSize = swapSpaceSize();
        physicalSpaceSize = physicalSpaceSize();
        committedVirtualMemorySize = committedVirtualMemorySize();

        memPoolInfos = jvmStats.getMem().getPools();
        gcInfoList = jvmStats.getGc();

        garbageCollectionTimeMillis = 0;
        for (JvmStats.GarbageCollector gc : gcInfoList) {
            garbageCollectionTimeMillis += gc.getCollectionTime().getDuration();
        }

        bufferPools = jvmStats.getBufferPools();

        return this;
    }

    public JvmStats.JvmMemoryInfo getJvmMemoryInfo() {
        return jvmMemoryInfo;
    }

    public MemoryUsage getHeapMemoryUsage() {
        return heapMemoryUsage;
    }

    public MemoryUsage getNonHeapMemoryUsage() {
        return nonHeapMemoryUsage;
    }

    public MemoryUsage getPermGenMemoryUsage() {
        return permGenMemoryUsage;
    }

    public int getLoadedClassesCount() {
        return loadedClassesCount;
    }

    public long getGarbageCollectionTimeMillis() {
        return garbageCollectionTimeMillis;
    }

    public MemorySize getSwapSpaceSize() {
        return swapSpaceSize;
    }

    public MemorySize getPhysicalSpaceSize() {
        return physicalSpaceSize;
    }

    public long getCommittedVirtualMemorySize() {
        return committedVirtualMemorySize;
    }

    public List<JvmStats.MemoryPool> getMemPoolInfos() {
        return memPoolInfos;
    }

    public List<JvmStats.GarbageCollector> getGcInfoList() {
        return gcInfoList;
    }

    public List<JvmStats.BufferPool> getBufferPools() {
        return bufferPools;
    }

    private MemoryUsage permGenMemoryUsage() {
        MemoryPoolMXBean memoryPoolMXBeanByName = null;
        if (Constants.IS_JAVA_1_7) {
            memoryPoolMXBeanByName = getMemoryPoolMXBeanByName("Perm Gen");
        } else if (Constants.IS_JAVA_1_8) {
            memoryPoolMXBeanByName = getMemoryPoolMXBeanByName("Metaspace");
        }
        if (memoryPoolMXBeanByName == null) {
            return MemoryUsage.negative();
        } else {
            return new MemoryUsage().clone(memoryPoolMXBeanByName.getUsage());
        }
    }

    private MemorySize swapSpaceSize() {
        OperatingSystemMXBean mxBean = ManagementFactory.getOperatingSystemMXBean();
        if (isSunJvm(mxBean)) {
            Long totalSize = getLongFromOperationSystemMXBean(mxBean, "getTotalSwapSpaceSize");
            Long freeSize = getLongFromOperationSystemMXBean(mxBean, "getFreeSwapSpaceSize");
            Long usedSize = totalSize - freeSize;
            return new MemorySize(totalSize, usedSize, freeSize);
        }
        return new MemorySize(-1, -1, -1);
    }

    private MemorySize physicalSpaceSize() {
        OperatingSystemMXBean mxBean = ManagementFactory.getOperatingSystemMXBean();
        if (isSunJvm(mxBean)) {
            Long totalSize = getLongFromOperationSystemMXBean(mxBean, "getTotalPhysicalMemorySize");
            Long freeSize = getLongFromOperationSystemMXBean(mxBean, "getFreePhysicalMemorySize");
            Long usedSize = totalSize - freeSize;
            return new MemorySize(totalSize, usedSize, freeSize);
        }
        return new MemorySize(-1, -1, -1);
    }

    private Long getLongFromOperationSystemMXBean(OperatingSystemMXBean mxBean, String methodName) {
        try {
            return (Long) mxBean.getClass().getMethod(methodName).invoke(mxBean, methodName);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage(), e);
        }
        return -1L;
    }

    private boolean isSunJvm(OperatingSystemMXBean mxBean) {
        return "com.sun.management.UnixOperatingSystem".equals(mxBean.getClass().getName());
    }

    private MemoryPoolMXBean getMemoryPoolMXBeanByName(String name) {
        try {
            for (MemoryPoolMXBean mxBean : ManagementFactory.getMemoryPoolMXBeans()) {
                if (mxBean.getName().endsWith(name)) {
                    return mxBean;
                }
            }
        } catch (Throwable t) {
            logger.error("Please check your JVM version and vendor", t);
        }
        return null;
    }

    private long committedVirtualMemorySize() {
        OperatingSystemMXBean mxBean = ManagementFactory.getOperatingSystemMXBean();
        if (isSunJvm(mxBean)) {
            return getLongFromOperationSystemMXBean(mxBean, "getCommittedVirtualMemorySize");
        }
        return -1;
    }
}
