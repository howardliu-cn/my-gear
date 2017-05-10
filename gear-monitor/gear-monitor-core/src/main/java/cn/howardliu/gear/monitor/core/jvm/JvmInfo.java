package cn.howardliu.gear.monitor.core.jvm;

import cn.howardliu.gear.monitor.core.unit.ByteSizeValue;

import java.lang.management.*;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static cn.howardliu.gear.monitor.core.Constants.*;

/**
 * <br>created at 16-12-27
 *
 * @author liuxh
 * @since 1.0.2
 */
public class JvmInfo {
    private static final JvmInfo INSTANCE = new JvmInfo();
    private static final String[] gcCollectors;
    private static final String[] memoryPools;

    static {
        List<GarbageCollectorMXBean> gcMxBeans = ManagementFactory.getGarbageCollectorMXBeans();
        gcCollectors = new String[gcMxBeans.size()];
        for (int i = 0; i < gcMxBeans.size(); i++) {
            GarbageCollectorMXBean gcMxBean = gcMxBeans.get(i);
            gcCollectors[i] = gcMxBean.getName();
        }

        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        memoryPools = new String[memoryPoolMXBeans.size()];
        for (int i = 0; i < memoryPoolMXBeans.size(); i++) {
            MemoryPoolMXBean memoryPoolMXBean = memoryPoolMXBeans.get(i);
            memoryPools[i] = memoryPoolMXBean.getName();
        }
    }

    private RuntimeInfo runtimeInfo = RuntimeInfo.instance();
    private VMOptionInfo vmOptionInfo = VMOptionInfo.instance();
    private MemoryInfo staticMemoryInfo = MemoryInfo.instance();

    private JvmInfo() {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new ManagementPermission("monitor"));
            sm.checkPropertyAccess("*");
        }
        instance();
    }

    public static JvmInfo instance() {
        return INSTANCE;
    }

    public RuntimeInfo getRuntimeInfo() {
        return runtimeInfo;
    }

    public void setRuntimeInfo(RuntimeInfo runtimeInfo) {
        this.runtimeInfo = runtimeInfo;
    }

    public VMOptionInfo getVmOptionInfo() {
        return vmOptionInfo;
    }

    public void setVmOptionInfo(VMOptionInfo vmOptionInfo) {
        this.vmOptionInfo = vmOptionInfo;
    }

    public MemoryInfo getStaticMemoryInfo() {
        return staticMemoryInfo;
    }

    public void setStaticMemoryInfo(MemoryInfo staticMemoryInfo) {
        this.staticMemoryInfo = staticMemoryInfo;
    }

    public String[] getGcCollectors() {
        return gcCollectors;
    }

    public String[] getMemoryPools() {
        return memoryPools;
    }

    public static class RuntimeInfo {
        private static final RuntimeInfo INSTANCE = new RuntimeInfo();
        private final int pid = PID.getPID();
        private final String name;
        private final String version = JAVA_VERSION;
        private final String vmName = JVM_NAME;
        private final String vmVersion = JVM_VERSION;
        private final String vmVendor = JVM_VENDOR;
        private final String managementSpecVersion;
        private final String specName = JVM_SPEC_NAME;
        private final String specVersion = JVM_SPEC_VERSION;
        private final String specVendor = JVM_SPEC_VENDOR;
        private final List<String> inputArguments;
        private final boolean bootClassPathSupported;
        private final String classPath = JAVA_CLASS_PATH;
        private final String libraryPath = JAVA_LIBRARY_PATH;
        private final Map<String, String> systemProperties;
        private final long startTime;
        private String bootClassPath = "";

        private RuntimeInfo() {
            RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
            name = mxBean.getName();
            managementSpecVersion = mxBean.getManagementSpecVersion();
            startTime = mxBean.getStartTime();
            inputArguments = mxBean.getInputArguments();
            bootClassPathSupported = mxBean.isBootClassPathSupported();
            if (bootClassPathSupported) {
                bootClassPath = mxBean.getBootClassPath();
            }
            systemProperties = mxBean.getSystemProperties();
        }

        static RuntimeInfo instance() {
            return INSTANCE;
        }

        public int getPid() {
            return pid;
        }

        public String getName() {
            return name;
        }

        public String getVersion() {
            return version;
        }

        public String getVmName() {
            return vmName;
        }

        public String getVmVersion() {
            return vmVersion;
        }

        public String getVmVendor() {
            return vmVendor;
        }

        public long getStartTime() {
            return startTime;
        }

        public long getUpTime() {
            return ManagementFactory.getRuntimeMXBean().getUptime();
        }

        public String getManagementSpecVersion() {
            return managementSpecVersion;
        }

        public String getSpecName() {
            return specName;
        }

        public String getSpecVersion() {
            return specVersion;
        }

        public String getSpecVendor() {
            return specVendor;
        }

        public List<String> getInputArguments() {
            return inputArguments;
        }

        public boolean isBootClassPathSupported() {
            return bootClassPathSupported;
        }

        public String getBootClassPath() {
            return bootClassPath;
        }

        public String getClassPath() {
            return classPath;
        }

        public String getLibraryPath() {
            return libraryPath;
        }

        public Map<String, String> getSystemProperties() {
            return systemProperties;
        }
    }

    public static class VMOptionInfo {
        private static final VMOptionInfo INSTANCE = new VMOptionInfo();
        private String onError = null;
        private String onOutOfMemoryError = null;
        private String useCompressedOops = "unknown";
        private String useG1GC = "unknown";
        private String useSerialGC = "unknown";
        private long configuredInitialHeapSize = -1;
        private long configuredMaxHeapSize = -1;

        {
            try {
                //noinspection unchecked
                Class<? extends PlatformManagedObject> clazz =
                        (Class<? extends PlatformManagedObject>) Class
                                .forName("com.sun.management.HotSpotDiagnosticMXBean");
                Class<?> vmOptionClazz = Class.forName("com.sun.management.VMOption");
                PlatformManagedObject hotSpotDiagnosticMXBean = ManagementFactory.getPlatformMXBean(clazz);
                Method vmOptionMethod = clazz.getMethod("getVMOption", String.class);
                Method valueMethod = vmOptionClazz.getMethod("getValue");

                try {
                    Object onErrorObject = vmOptionMethod.invoke(hotSpotDiagnosticMXBean, "OnError");
                    onError = (String) valueMethod.invoke(onErrorObject);
                } catch (Exception ignored) {
                }

                try {
                    Object onOutOfMemoryErrorObject = vmOptionMethod
                            .invoke(hotSpotDiagnosticMXBean, "OnOutOfMemoryError");
                    onOutOfMemoryError = (String) valueMethod.invoke(onOutOfMemoryErrorObject);
                } catch (Exception ignored) {
                }

                try {
                    Object useCompressedOopsVmOptionObject = vmOptionMethod
                            .invoke(hotSpotDiagnosticMXBean, "UseCompressedOops");
                    useCompressedOops = (String) valueMethod.invoke(useCompressedOopsVmOptionObject);
                } catch (Exception ignored) {
                }

                try {
                    Object useG1GCVmOptionObject = vmOptionMethod.invoke(hotSpotDiagnosticMXBean, "UseG1GC");
                    useG1GC = (String) valueMethod.invoke(useG1GCVmOptionObject);
                } catch (Exception ignored) {
                }

                try {
                    Object initialHeapSizeVmOptionObject = vmOptionMethod
                            .invoke(hotSpotDiagnosticMXBean, "InitialHeapSize");
                    configuredInitialHeapSize = Long
                            .parseLong((String) valueMethod.invoke(initialHeapSizeVmOptionObject));
                } catch (Exception ignored) {
                }

                try {
                    Object maxHeapSizeVmOptionObject = vmOptionMethod.invoke(hotSpotDiagnosticMXBean, "MaxHeapSize");
                    configuredMaxHeapSize = Long.parseLong((String) valueMethod.invoke(maxHeapSizeVmOptionObject));
                } catch (Exception ignored) {
                }

                try {
                    Object useSerialGCVmOptionObject = vmOptionMethod.invoke(hotSpotDiagnosticMXBean, "UseSerialGC");
                    useSerialGC = (String) valueMethod.invoke(useSerialGCVmOptionObject);
                } catch (Exception ignored) {
                }
            } catch (Exception ignored) {
            }
        }

        private VMOptionInfo() {
        }

        static VMOptionInfo instance() {
            return INSTANCE;
        }

        public String getOnError() {
            return onError;
        }

        public String getOnOutOfMemoryError() {
            return onOutOfMemoryError;
        }

        public String getUseCompressedOops() {
            return useCompressedOops;
        }

        public String getUseG1GC() {
            return useG1GC;
        }

        public String getUseSerialGC() {
            return useSerialGC;
        }

        public long getConfiguredInitialHeapSize() {
            return configuredInitialHeapSize;
        }

        public long getConfiguredMaxHeapSize() {
            return configuredMaxHeapSize;
        }
    }

    public static class MemoryInfo {
        private static final MemoryInfo INSTANCE = new MemoryInfo();
        private final long heapInit;
        private final long heapMax;
        private final long nonHeapInit;
        private final long nonHeapMax;
        private long directMemoryMax;

        {
            MemoryMXBean mxBean = ManagementFactory.getMemoryMXBean();
            heapInit = mxBean.getHeapMemoryUsage().getInit() < 0 ? 0 : mxBean.getHeapMemoryUsage().getInit();
            heapMax = mxBean.getHeapMemoryUsage().getMax() < 0 ? 0 : mxBean.getHeapMemoryUsage().getMax();
            nonHeapInit = mxBean.getNonHeapMemoryUsage().getInit() < 0 ? 0 : mxBean.getNonHeapMemoryUsage().getInit();
            nonHeapMax = mxBean.getNonHeapMemoryUsage().getMax() < 0 ? 0 : mxBean.getNonHeapMemoryUsage().getMax();
            try {
                Class<?> vmClass = Class.forName("sun.misc.VM");
                directMemoryMax = (Long) vmClass.getMethod("maxDirectMemory").invoke(null);
            } catch (Exception ignored) {
            }
        }

        static MemoryInfo instance() {
            return INSTANCE;
        }

        public ByteSizeValue getHeapInit() {
            return new ByteSizeValue(heapInit);
        }

        public ByteSizeValue getHeapMax() {
            return new ByteSizeValue(heapMax);
        }

        public ByteSizeValue getNonHeapInit() {
            return new ByteSizeValue(nonHeapInit);
        }

        public ByteSizeValue getNonHeapMax() {
            return new ByteSizeValue(nonHeapMax);
        }

        public ByteSizeValue getDirectMemoryMax() {
            return new ByteSizeValue(directMemoryMax);
        }
    }
}
