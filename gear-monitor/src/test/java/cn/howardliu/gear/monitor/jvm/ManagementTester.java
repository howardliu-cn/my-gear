package cn.howardliu.gear.monitor.jvm;

import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.VMOption;
import jdk.management.cmm.SystemResourcePressureMXBean;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.management.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * <br>created at 16-12-15
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ManagementTester {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void test() throws Exception {
        ManagementFactory.getPlatformMXBeans(null);
        ManagementFactory.getPlatformMXBean(null);
        ManagementFactory.getPlatformMXBean(null, null);
        ManagementFactory.getPlatformMXBeans(null, null);
    }

    @Test
    public void testClassLoadingMXBean() {
        System.out.println("Class Loader Management:");
        ClassLoadingMXBean mxBean = ManagementFactory.getClassLoadingMXBean();
        System.out.println("loaded class count: " + mxBean.getLoadedClassCount());
        System.out.println("total loaded class count: " + mxBean.getTotalLoadedClassCount());
        System.out.println("unloaded class count: " + mxBean.getUnloadedClassCount());
    }

    @Test
    public void testCompilationMXBean() {
        System.out.println("Compilation Management:");
        CompilationMXBean mxBean = ManagementFactory.getCompilationMXBean();
        System.out.println("name: " + mxBean.getName());
        System.out.println("total compilation time: " + mxBean.getTotalCompilationTime());
        System.out.println("canonical name: " + mxBean.getObjectName().getCanonicalName());
    }

    @Test
    public void testGarbageCollectorMXBeans() {
        System.out.println("Garbage Collector Management:");
        List<GarbageCollectorMXBean> mxBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean mxBean : mxBeans) {
            System.out.println("==========================================");
            System.out.println("name: " + mxBean.getName());
            System.out.println("collection count: " + mxBean.getCollectionCount());
            System.out.println("collection time: " + mxBean.getCollectionTime());
            System.out.println("memory pool names: " + Arrays.toString(mxBean.getMemoryPoolNames()));
            System.out.println("canonical name: " + mxBean.getObjectName().getCanonicalName());
        }
    }

    @Test
    public void testMemoryManagerMXBeans() {
        System.out.println("memory manager Management:");
        List<MemoryManagerMXBean> mxBeans = ManagementFactory.getMemoryManagerMXBeans();
        for (MemoryManagerMXBean mxBean : mxBeans) {
            System.out.println("==========================================");
            System.out.println("name: " + mxBean.getName());
            System.out.println("memory pool names: " + Arrays.toString(mxBean.getMemoryPoolNames()));
            System.out.println("canonical name: " + mxBean.getObjectName().getCanonicalName());
        }
    }

    @Test
    public void testMemoryMXBean() {
        System.out.println("memory Management:");
        MemoryMXBean mxBean = ManagementFactory.getMemoryMXBean();
        System.out.println("heap memory usage: " + mxBean.getHeapMemoryUsage());
        System.out.println("no heap memory usage: " + mxBean.getNonHeapMemoryUsage());
        System.out.println("object pending finalization count: " + mxBean.getObjectPendingFinalizationCount());
        System.out.println("canonical name: " + mxBean.getObjectName().getCanonicalName());
    }

    @Test
    public void testMemoryPoolMXBeans() {
        System.out.println("memory pool Management:");
        List<MemoryPoolMXBean> mxBeans = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean mxBean : mxBeans) {
            System.out.println("==========================================");
            System.out.println("name:" + mxBean.getName());
            System.out.println("type: " + mxBean.getType());
            System.out.println("valid: " + mxBean.isValid());
            System.out.println("memory manager names: " + Arrays.toString(mxBean.getMemoryManagerNames()));
            System.out.println("peak memory usage: " + mxBean.getPeakUsage());
            System.out.println("memory usage: " + mxBean.getUsage());
            System.out.println("memory usage threshold supported: " + mxBean.isUsageThresholdSupported());
            if (mxBean.isUsageThresholdSupported()) {
                System.out.println("memory usage threshold: " + mxBean.getUsageThreshold());
                System.out.println("memory usage threshold count: " + mxBean.getUsageThresholdCount());
                System.out.println("memory usage threshold exceeded: " + mxBean.isUsageThresholdExceeded());
            }
            System.out.println("collection usage: " + mxBean.getCollectionUsage());
            System.out.println("collection usage threshold supported: " + mxBean.isCollectionUsageThresholdSupported());
            if (mxBean.isCollectionUsageThresholdSupported()) {
                System.out.println("collection usage threshold: " + mxBean.getCollectionUsageThreshold());
                System.out.println("collection usage threshold count: " + mxBean.getCollectionUsageThresholdCount());
                System.out
                        .println("collection usage threshold exceeded: " + mxBean.isCollectionUsageThresholdExceeded());
            }
            System.out.println("canonical name: " + mxBean.getObjectName().getCanonicalName());
        }
    }

    @Test
    public void testOperatingSystemMXBean() {
        System.out.println("operating system Management:");
        OperatingSystemMXBean mxBean = ManagementFactory.getOperatingSystemMXBean();
        System.out.println("architecture: " + mxBean.getArch());
        System.out.println("available processors: " + mxBean.getAvailableProcessors());
        System.out.println("system name: " + mxBean.getName());
        System.out.println("system version: " + mxBean.getVersion());
        System.out.println("system load average: " + mxBean.getSystemLoadAverage());
        System.out.println("canonical name: " + mxBean.getObjectName().getCanonicalName());
    }

    @Test
    public void testPlatformManagementInterfaces() {
        Set<Class<? extends PlatformManagedObject>> interfaces = ManagementFactory.getPlatformManagementInterfaces();
        for (Class<? extends PlatformManagedObject> anInterface : interfaces) {
            System.out.println(anInterface.getCanonicalName());
        }
    }

    @Test
    public void testPlatformMBeanServer() {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        System.out.println("default domain: " + server.getDefaultDomain());
        System.out.println("domain: " + Arrays.toString(server.getDomains()));
        System.out.println("mBean count: " + server.getMBeanCount());
    }

    @Test
    public void testRuntimeMXBean() {
        System.out.println("runtime Management:");
        RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
        System.out.println("name: " + mxBean.getName());
        System.out.println("start time: " + mxBean.getStartTime());
        System.out.println("up time: " + mxBean.getUptime());
        System.out.println("input arguments: " + mxBean.getInputArguments());
        System.out.println("vm name: " + mxBean.getVmName());
        System.out.println("vm vendor: " + mxBean.getVmVendor());
        System.out.println("vm version: " + mxBean.getVmVersion());
        System.out.println("management specification version: " + mxBean.getManagementSpecVersion());
        System.out.println("specification name: " + mxBean.getSpecName());
        System.out.println("specification vendor: " + mxBean.getSpecVendor());
        System.out.println("specification version: " + mxBean.getSpecVersion());
        if (mxBean.isBootClassPathSupported()) {
            System.out.println("boot class path: " + mxBean.getBootClassPath());
        }
        System.out.println("class path: " + mxBean.getClassPath());
        System.out.println("library path: " + mxBean.getLibraryPath());
        System.out.println("system properties: " + mxBean.getSystemProperties());
        System.out.println("canonical name: " + mxBean.getObjectName().getCanonicalName());
    }

    @Test
    public void testThreadMXBean() {
        System.out.println("thread Management:");
        ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
//        ThreadInfo[] threadInfos = mxBean.dumpAllThreads(mxBean.isObjectMonitorUsageSupported(),
//                mxBean.isSynchronizerUsageSupported());
        System.out.println("dead locked threads' id: " + Arrays.toString(mxBean.findDeadlockedThreads()));
        System.out
                .println("monitor dead locked threads' id: " + Arrays.toString(mxBean.findMonitorDeadlockedThreads()));
        System.out.println("current thread cpu time: " + mxBean.getCurrentThreadCpuTime());
        System.out.println("current thread user time: " + mxBean.getCurrentThreadUserTime());
        System.out.println("thread count: " + mxBean.getThreadCount());
        System.out.println("peak thread count: " + mxBean.getPeakThreadCount());
        System.out.println("daemon thread count: " + mxBean.getDaemonThreadCount());
        System.out.println("total started thread count: " + mxBean.getTotalStartedThreadCount());
        System.out.println("current thread cpu time supported: " + mxBean.isCurrentThreadCpuTimeSupported());
        System.out.println("object monitor usage supported: " + mxBean.isObjectMonitorUsageSupported());
        System.out.println("synchronizer usage supported: " + mxBean.isSynchronizerUsageSupported());
        System.out.println("thread contention monitoring enabled: " + mxBean.isThreadContentionMonitoringEnabled());
        System.out.println("thread contention monitoring supported: " + mxBean.isThreadContentionMonitoringSupported());
        System.out.println("thread cpu time enabled: " + mxBean.isThreadCpuTimeEnabled());
        System.out.println("thread cpu time supported: " + mxBean.isThreadCpuTimeSupported());
        long[] allThreadIds = mxBean.getAllThreadIds();
        System.out.println("all threads' id: " + Arrays.toString(allThreadIds));
        for (long threadId : allThreadIds) {
            System.out.println("thread id is " + threadId + ", thread cpu time is "
                    + mxBean.getThreadCpuTime(threadId) + "(ns), thread user time is "
                    + mxBean.getThreadUserTime(threadId));
            System.out.println(mxBean.getThreadInfo(threadId, 1));
        }
    }

    @Test
    public void testGetInfoByMBeanServer() throws MalformedObjectNameException, IntrospectionException,
            InstanceNotFoundException, ReflectionException, AttributeNotFoundException, MBeanException {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = server.queryNames(new ObjectName("*:type=OperatingSystem,*"), null);
        for (ObjectName objectName : objectNames) {
            MBeanInfo mBeanInfo = server.getMBeanInfo(objectName);
            MBeanAttributeInfo[] attributes = mBeanInfo.getAttributes();
            for (MBeanAttributeInfo attribute : attributes) {
                System.out.println(attribute.getName() + ": " + server.getAttribute(objectName, attribute.getName()));
            }
        }
    }

    @Test
    public void testBufferPollMXBean() {
        List<BufferPoolMXBean> bufferPoolMXBeans = ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class);
        for (BufferPoolMXBean mxBean : bufferPoolMXBeans) {
            System.out.println("==========================================");
            System.out.println("name: " + mxBean.getName());
            System.out.println("count: " + mxBean.getCount());
            System.out.println("total capacity: " + mxBean.getTotalCapacity());
            System.out.println("memory usage: " + mxBean.getMemoryUsed());
        }
    }

    @Test
    public void testHotSpotDiagnosticMXBean() {
        HotSpotDiagnosticMXBean mxBean = ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);
        List<VMOption> options = mxBean.getDiagnosticOptions();
        for (VMOption option : options) {
            System.out.println("==========================================");
            System.out.println("name: " + option.getName());
            System.out.println("origin: " + option.getOrigin());
            System.out.println("value: "+ option.getValue());
            System.out.println("isWriteable: " + option.isWriteable());
        }
    }
}
