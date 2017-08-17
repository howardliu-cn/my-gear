package cn.howardliu.gear.monitor.core.jvm;

import cn.howardliu.gear.monitor.core.Constants;
import cn.howardliu.gear.monitor.core.Stats;
import cn.howardliu.gear.monitor.core.memory.MemoryUsage;
import cn.howardliu.gear.monitor.core.unit.ByteSizeValue;
import cn.howardliu.gear.monitor.core.unit.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <br>created at 17-2-21
 *
 * @author liuxh
 * @since 1.0.2
 */
public class JvmStats extends Stats {
    private static final Logger logger = LoggerFactory.getLogger(JvmStats.class);
    private static final RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    private static final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    private static final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private static final ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
    private static final CompilationMXBean compilationMXBean = ManagementFactory.getCompilationMXBean();

    private final long uptime;
    private final Mem mem;
    private final Threads threads;
    private final List<GarbageCollector> gc;
    private final List<BufferPool> bufferPools;
    private final Classes classes;
    private final CompilationInfo compilationInfo;

    private JvmStats(long timestamp, long uptime, Mem mem, Threads threads,
            List<GarbageCollector> gc, List<BufferPool> bufferPools, Classes classes, CompilationInfo compilationInfo) {
        super(timestamp);
        this.uptime = uptime;
        this.mem = mem;
        this.threads = threads;
        this.gc = gc;
        this.bufferPools = bufferPools;
        this.classes = classes;
        this.compilationInfo = compilationInfo;
    }

    public static JvmStats stats() {
        return new JvmStats(
                System.currentTimeMillis(),
                runtimeMXBean.getUptime(),
                mem(),
                threads(),
                garbageCollectors(),
                bufferPoolsList(),
                classes(),
                compilationInfo()
        );
    }

    private static Mem mem() {
        return new Mem(
                getJvmMemoryInfo(),
                new MemoryUsage().clone(memoryMXBean.getHeapMemoryUsage()),
                new MemoryUsage().clone(memoryMXBean.getNonHeapMemoryUsage()),
                permGenMemoryUsage(),
                getMemoryPools()
        );
    }

    private static MemoryUsage permGenMemoryUsage() {
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

    private static MemoryPoolMXBean getMemoryPoolMXBeanByName(String name) {
        try {
            for (MemoryPoolMXBean mxBean : ManagementFactory.getMemoryPoolMXBeans()) {
                if (mxBean.getName().endsWith(name)) {
                    return mxBean;
                }
            }
        } catch (Throwable t) {
            if (logger.isDebugEnabled()) {
                logger.debug("Please check your JVM version and vendor", t);
            }
        }
        return null;
    }

    private static List<MemoryPool> getMemoryPools() {
        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        List<MemoryPool> memoryPools = new ArrayList<>(memoryPoolMXBeans.size());
        for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
            try {
                String name = memoryPoolMXBean.getName();
                MemoryType type = memoryPoolMXBean.getType();
                memoryPools.add(
                        new MemoryPool(
                                name, type.name(),
                                new MemoryUsage().clone(memoryPoolMXBean.getUsage()),
                                new MemoryUsage().clone(memoryPoolMXBean.getPeakUsage()),
                                new MemoryUsage().clone(memoryPoolMXBean.getCollectionUsage())
                        )
                );
            } catch (Exception ignored) {
            }
        }
        return memoryPools;
    }

    private static Threads threads() {
        return new Threads(
                threadMXBean.getThreadCount(),
                threadMXBean.getPeakThreadCount(),
                threadMXBean.getTotalStartedThreadCount(),
                threadMXBean.getDaemonThreadCount(),
                threadMXBean.getCurrentThreadCpuTime(),
                threadMXBean.getCurrentThreadUserTime()
        );
    }

    private static List<GarbageCollector> garbageCollectors() {
        List<GarbageCollectorMXBean> gcMxBeans = ManagementFactory.getGarbageCollectorMXBeans();
        List<GarbageCollector> garbageCollectors = new ArrayList<>(gcMxBeans.size());
        for (GarbageCollectorMXBean gcMxBean : gcMxBeans) {
            garbageCollectors.add(
                    new GarbageCollector(
                            gcMxBean.getName(),
                            gcMxBean.getCollectionCount(),
                            gcMxBean.getCollectionTime()
                    )
            );
        }
        return garbageCollectors;
    }

    private static List<BufferPool> bufferPoolsList() {
        List<BufferPool> bufferPoolsList = Collections.emptyList();
        try {
            List<BufferPoolMXBean> bufferPools = ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class);
            bufferPoolsList = new ArrayList<>(bufferPools.size());
            for (BufferPoolMXBean bufferPool : bufferPools) {
                bufferPoolsList.add(
                        new BufferPool(
                                bufferPool.getName(), bufferPool.getCount(),
                                bufferPool.getTotalCapacity(), bufferPool.getMemoryUsed()
                        )
                );
            }
        } catch (Exception ignored) {
        }
        return bufferPoolsList;
    }

    private static Classes classes() {
        return new Classes(classLoadingMXBean.getLoadedClassCount(),
                classLoadingMXBean.getTotalLoadedClassCount(),
                classLoadingMXBean.getUnloadedClassCount());
    }

    private static JvmMemoryInfo getJvmMemoryInfo() {
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = Runtime.getRuntime().maxMemory();
        return new JvmMemoryInfo(totalMemory, freeMemory, usedMemory, maxMemory);
    }

    private static CompilationInfo compilationInfo() {
        return new CompilationInfo(compilationMXBean.getName(), compilationMXBean.getTotalCompilationTime());
    }

    public long getUptime() {
        return uptime;
    }

    public TimeValue getUptimeValue() {
        return new TimeValue(uptime);
    }

    public Mem getMem() {
        return this.mem;
    }

    public Threads getThreads() {
        return threads;
    }

    public List<GarbageCollector> getGc() {
        return gc;
    }

    public List<BufferPool> getBufferPools() {
        return bufferPools;
    }

    public Classes getClasses() {
        return classes;
    }

    public CompilationInfo getCompilationInfo() {
        return compilationInfo;
    }

    public static class Classes {
        private final int loadedClassCount;
        private final long totalLoadedClassCount;
        private final long unloadedClassCount;

        Classes(int loadedClassCount, long totalLoadedClassCount, long unloadedClassCount) {
            this.loadedClassCount = loadedClassCount;
            this.totalLoadedClassCount = totalLoadedClassCount;
            this.unloadedClassCount = unloadedClassCount;
        }

        public int getLoadedClassCount() {
            return loadedClassCount;
        }

        public long getTotalLoadedClassCount() {
            return totalLoadedClassCount;
        }

        public long getUnloadedClassCount() {
            return unloadedClassCount;
        }
    }

    public static class BufferPool {
        private final String name;
        private final long count;
        private final long totalCapacity;
        private final long used;

        BufferPool(String name, long count, long totalCapacity, long used) {
            this.name = name;
            this.count = count;
            this.totalCapacity = totalCapacity;
            this.used = used;
        }

        public String getName() {
            return this.name;
        }

        public long getCount() {
            return this.count;
        }

        public long getTotalCapacity() {
            return totalCapacity;
        }

        public long getUsed() {
            return used;
        }

        public ByteSizeValue getTotalCapacityValue() {
            return new ByteSizeValue(totalCapacity);
        }

        public ByteSizeValue getUsedValue() {
            return new ByteSizeValue(used);
        }
    }

    public static class Mem implements Iterable<MemoryPool> {
        private final JvmMemoryInfo jvmMemoryInfo;
        private final MemoryUsage heapMemoryUsage;
        private final MemoryUsage nonHeapMemoryUsage;
        private final MemoryUsage permGenMemoryUsage;
        private final List<MemoryPool> pools;

        Mem(JvmMemoryInfo jvmMemoryInfo, MemoryUsage heapMemoryUsage, MemoryUsage nonHeapMemoryUsage,
                MemoryUsage permGenMemoryUsage, List<MemoryPool> pools) {
            this.jvmMemoryInfo = jvmMemoryInfo;
            this.heapMemoryUsage = heapMemoryUsage;
            this.nonHeapMemoryUsage = nonHeapMemoryUsage;
            this.permGenMemoryUsage = permGenMemoryUsage;
            this.pools = pools;
        }

        @Override
        public Iterator<MemoryPool> iterator() {
            return pools.iterator();
        }

        public List<MemoryPool> getPools() {
            return pools;
        }

        public JvmMemoryInfo getJvmMemoryInfo() {
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
    }

    public static class GarbageCollector {
        private final String name;
        private final String gcName;
        private final long collectionCount;
        private final long collectionTime;

        GarbageCollector(String name, long collectionCount, long collectionTime) {
            this.name = name;
            this.gcName = GcNames.getByGcName(name, name);
            this.collectionCount = collectionCount;
            this.collectionTime = collectionTime;
        }

        public String getName() {
            return this.name;
        }

        public String getGcName() {
            return gcName;
        }

        public long getCollectionCount() {
            return this.collectionCount;
        }

        public long getCollectionTime() {
            return collectionTime;
        }

        public TimeValue getCollectionTimeValue() {
            return new TimeValue(collectionTime, TimeUnit.MILLISECONDS);
        }
    }

    public static class Threads {
        private final int count;
        private final int peakCount;
        private final long totalStartedCount;
        private final int daemonCount;
        private final long currentCpuTime;
        private final long currentUserTime;

        Threads(int count, int peakCount, long totalStartedCount, int daemonCount,
                long currentCpuTime, long currentUserTime) {
            this.count = count;
            this.peakCount = peakCount;
            this.totalStartedCount = totalStartedCount;
            this.daemonCount = daemonCount;
            this.currentCpuTime = currentCpuTime;
            this.currentUserTime = currentUserTime;
        }

        public int getCount() {
            return count;
        }

        public int getPeakCount() {
            return peakCount;
        }

        public long getTotalStartedCount() {
            return totalStartedCount;
        }

        public int getDaemonCount() {
            return daemonCount;
        }

        public long getCurrentCpuTime() {
            return currentCpuTime;
        }

        public long getCurrentUserTime() {
            return currentUserTime;
        }
    }

    public static class JvmMemoryInfo {
        private final long totalMemory;
        private final long freeMemory;
        private final long usedMemory;
        private final long maxMemory;

        JvmMemoryInfo(long totalMemory, long freeMemory, long usedMemory, long maxMemory) {
            this.totalMemory = totalMemory;
            this.freeMemory = freeMemory;
            this.usedMemory = usedMemory;
            this.maxMemory = maxMemory;
        }

        public long getTotalMemory() {
            return totalMemory;
        }

        public long getFreeMemory() {
            return freeMemory;
        }

        public long getUsedMemory() {
            return usedMemory;
        }

        public long getMaxMemory() {
            return maxMemory;
        }

        public ByteSizeValue getTotalMemoryValue() {
            return new ByteSizeValue(totalMemory);
        }

        public ByteSizeValue getFreeMemoryValue() {
            return new ByteSizeValue(freeMemory);
        }

        public ByteSizeValue getUsedMemoryValue() {
            return new ByteSizeValue(usedMemory);
        }

        public ByteSizeValue getMaxMemoryValue() {
            return new ByteSizeValue(maxMemory);
        }
    }

    public static class MemoryPool {
        private final String name;
        // TODO remove memoryManagerNames field
        private final String memoryManagerNames;
        private final String gcName;
        private final String type;
        private final MemoryUsage usage;
        private final MemoryUsage peakUsage;
        private final MemoryUsage collectionUsage;

        MemoryPool(String name, String type, MemoryUsage usage, MemoryUsage peakUsage, MemoryUsage collectionUsage) {
            this.name = name;
            this.memoryManagerNames = name;
            this.gcName = GcNames.getByMemoryPoolName(this.name, null);
            this.type = type;
            this.usage = usage;
            this.peakUsage = peakUsage;
            this.collectionUsage = collectionUsage;
        }

        public String getName() {
            return this.name;
        }

        public String getMemoryManagerNames() {
            return memoryManagerNames;
        }

        public String getGcName() {
            return gcName;
        }

        public String getType() {
            return type;
        }

        public MemoryUsage getUsage() {
            return usage;
        }

        public MemoryUsage getPeakUsage() {
            return peakUsage;
        }

        public MemoryUsage getCollectionUsage() {
            return collectionUsage;
        }

        public ByteSizeValue getUsed() {
            return new ByteSizeValue(usage.getUsed());
        }

        public ByteSizeValue getMax() {
            return new ByteSizeValue(usage.getMax());
        }

        public ByteSizeValue getPeakUsed() {
            return new ByteSizeValue(peakUsage.getUsed());
        }

        public ByteSizeValue getPeakMax() {
            return new ByteSizeValue(peakUsage.getMax());
        }

        public ByteSizeValue getCollectionUsed() {
            return new ByteSizeValue(collectionUsage.getUsed());
        }

        public ByteSizeValue getCollectionMax() {
            return new ByteSizeValue(collectionUsage.getMax());
        }
    }

    public static class CompilationInfo {
        private final String name;
        private final long compilationTime;

        CompilationInfo(String name, long compilationTime) {
            this.name = name;
            this.compilationTime = compilationTime;
        }

        public String getName() {
            return name;
        }

        public long getCompilationTime() {
            return compilationTime;
        }
    }
}
