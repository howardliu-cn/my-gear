package cn.howardliu.gear.monitor.jvm;

import cn.howardliu.gear.monitor.unit.ByteSizeValue;
import cn.howardliu.gear.monitor.unit.TimeValue;

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
public class JvmStats {
    private static final RuntimeMXBean runtimeMXBean;

    static {
        runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    }

    private final long timestamp;
    private final long uptime;
    private final Mem mem;
    private final Threads threads;
    private final List<GarbageCollector> gc;
    private final List<BufferPool> bufferPools;
    private final Classes classes;

    public JvmStats(long timestamp, long uptime, Mem mem, Threads threads,
            List<GarbageCollector> gc, List<BufferPool> bufferPools, Classes classes) {
        this.timestamp = timestamp;
        this.uptime = uptime;
        this.mem = mem;
        this.threads = threads;
        this.gc = gc;
        this.bufferPools = bufferPools;
        this.classes = classes;
    }

    public static JvmStats jvmStats() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();

        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        List<MemoryPool> memoryPools = new ArrayList<>(memoryPoolMXBeans.size());
        for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
            try {
                String name = memoryPoolMXBean.getName();
                MemoryType type = memoryPoolMXBean.getType();
                MemoryUsage usage = memoryPoolMXBean.getUsage();
                MemoryUsage peakUsage = memoryPoolMXBean.getPeakUsage();
                MemoryUsage collectionUsage = memoryPoolMXBean.getCollectionUsage();
                memoryPools.add(
                        new MemoryPool(
                                name, type.name(),
                                usage.getUsed(), usage.getMax(),
                                peakUsage.getUsed(), peakUsage.getMax(),
                                collectionUsage.getUsed(), collectionUsage.getMax()
                        )
                );
            } catch (Exception ignored) {
            }
        }

        Mem mem = new Mem(heapMemoryUsage.getCommitted(), heapMemoryUsage.getUsed(), heapMemoryUsage.getMax(),
                nonHeapMemoryUsage.getCommitted(), nonHeapMemoryUsage.getUsed(), memoryPools);

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        Threads threads = new Threads(threadMXBean.getThreadCount(), threadMXBean.getPeakThreadCount());

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

        ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
        Classes classes = new Classes(classLoadingMXBean.getLoadedClassCount(),
                classLoadingMXBean.getTotalLoadedClassCount(),
                classLoadingMXBean.getUnloadedClassCount());

        return new JvmStats(System.currentTimeMillis(), runtimeMXBean.getUptime(), mem, threads,
                garbageCollectors, bufferPoolsList, classes);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public TimeValue getUptime() {
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

    public static class Classes {
        private final long loadedClassCount;
        private final long totalLoadedClassCount;
        private final long unloadedClassCount;

        public Classes(long loadedClassCount, long totalLoadedClassCount, long unloadedClassCount) {
            this.loadedClassCount = loadedClassCount;
            this.totalLoadedClassCount = totalLoadedClassCount;
            this.unloadedClassCount = unloadedClassCount;
        }

        public long getLoadedClassCount() {
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

        public BufferPool(String name, long count, long totalCapacity, long used) {
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

        public ByteSizeValue getTotalCapacity() {
            return new ByteSizeValue(totalCapacity);
        }

        public ByteSizeValue getUsed() {
            return new ByteSizeValue(used);
        }
    }

    public static class Mem implements Iterable<MemoryPool> {
        private final long heapCommitted;
        private final long heapUsed;
        private final long heapMax;
        private final long nonHeapCommitted;
        private final long nonHeapUsed;
        private final List<MemoryPool> pools;

        public Mem(long heapCommitted, long heapUsed, long heapMax, long nonHeapCommitted, long nonHeapUsed,
                List<MemoryPool> pools) {
            this.heapCommitted = heapCommitted;
            this.heapUsed = heapUsed;
            this.heapMax = heapMax;
            this.nonHeapCommitted = nonHeapCommitted;
            this.nonHeapUsed = nonHeapUsed;
            this.pools = pools;
        }

        @Override
        public Iterator<MemoryPool> iterator() {
            return pools.iterator();
        }

        public ByteSizeValue getHeapCommitted() {
            return new ByteSizeValue(heapCommitted);
        }

        public ByteSizeValue getHeapUsed() {
            return new ByteSizeValue(heapUsed);
        }

        public ByteSizeValue getHeapMax() {
            return new ByteSizeValue(heapMax);
        }

        public short getHeapUsedPercent() {
            if (heapMax == 0) {
                return -1;
            }
            return (short) (heapUsed * 100 / heapMax);
        }

        public ByteSizeValue getNonHeapCommitted() {
            return new ByteSizeValue(nonHeapCommitted);
        }

        public ByteSizeValue getNonHeapUsed() {
            return new ByteSizeValue(nonHeapUsed);
        }
    }

    public static class GarbageCollector {
        private final String name;
        private final String gcName;
        private final long collectionCount;
        private final long collectionTime;

        public GarbageCollector(String name, long collectionCount, long collectionTime) {
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

        public TimeValue getCollectionTime() {
            return new TimeValue(collectionTime, TimeUnit.MILLISECONDS);
        }
    }

    public static class Threads {
        private final int count;
        private final int peakCount;

        public Threads(int count, int peakCount) {
            this.count = count;
            this.peakCount = peakCount;
        }

        public int getCount() {
            return count;
        }

        public int getPeakCount() {
            return peakCount;
        }
    }

    public static class MemoryPool {
        private final String name;
        private final String gcName;
        private final String type;
        private final long used;
        private final long max;
        private final long peakUsed;
        private final long peakMax;
        private final long collectionUsed;
        private final long collectionMax;

        public MemoryPool(String name, String type, long used, long max, long peakUsed, long peakMax,
                long collectionUsed, long collectionMax) {
            this.name = name;
            this.gcName = GcNames.getByMemoryPoolName(this.name, null);
            this.type = type;
            this.used = used;
            this.max = max;
            this.peakUsed = peakUsed;
            this.peakMax = peakMax;
            this.collectionUsed = collectionUsed;
            this.collectionMax = collectionMax;
        }

        public String getName() {
            return this.name;
        }

        public String getType() {
            return type;
        }

        public ByteSizeValue getUsed() {
            return new ByteSizeValue(used);
        }

        public ByteSizeValue getMax() {
            return new ByteSizeValue(max);
        }

        public ByteSizeValue getPeakUsed() {
            return new ByteSizeValue(peakUsed);
        }

        public ByteSizeValue getPeakMax() {
            return new ByteSizeValue(peakMax);
        }

        public ByteSizeValue getCollectionUsed() {
            return new ByteSizeValue(collectionUsed);
        }

        public ByteSizeValue getCollectionMax() {
            return new ByteSizeValue(collectionMax);
        }
    }
}
