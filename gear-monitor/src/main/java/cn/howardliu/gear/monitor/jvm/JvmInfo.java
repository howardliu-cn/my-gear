package cn.howardliu.gear.monitor.jvm;

import cn.howardliu.gear.monitor.unit.ByteSizeValue;

import java.lang.management.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <br>created at 16-12-27
 *
 * @author liuxh
 * @since 1.0.2
 */
public class JvmInfo {
    private RuntimeInfo runtimeInfo = RuntimeInfo.instance();
    private VMOptionInfo vmOptionInfo = VMOptionInfo.instance();
    private MemoryInfo staticMemoryInfo = MemoryInfo.instance();
    private String[] gcCollectors;
    private String[] memoryPools;

    private static JvmInfo INSTANCE = new JvmInfo();

    {
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

    public static JvmInfo instance() {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new ManagementPermission("monitor"));
            sm.checkPropertyAccess("*");
        }
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

    public void setGcCollectors(String[] gcCollectors) {
        this.gcCollectors = gcCollectors;
    }

    public String[] getMemoryPools() {
        return memoryPools;
    }

    public void setMemoryPools(String[] memoryPools) {
        this.memoryPools = memoryPools;
    }

    public static class RuntimeInfo {
        private int pid;
        private String name;
        private String version;
        private String vmName;
        private String vmVersion;
        private String vmVendor;
        private long startTime;
        private long upTime;
        private String managementSpecVersion;
        private String specName;
        private String specVersion;
        private String specVendor;
        private List<String> inputArguments = new ArrayList<>();
        private boolean bootClassPathSupported;
        private String bootClassPath = "";
        private String classPath;
        private String libraryPath;
        private Map<String, String> systemProperties;

        private static RuntimeInfo INSTANCE = new RuntimeInfo();

        {
            RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
            pid = PID.getPID();
            name = mxBean.getName();
            version = System.getProperty("java.version");
            vmName = mxBean.getVmName();
            vmVersion = mxBean.getVmVersion();
            vmVendor = mxBean.getVmVendor();
            managementSpecVersion = mxBean.getManagementSpecVersion();
            specName = mxBean.getSpecName();
            specVersion = mxBean.getSpecVersion();
            specVendor = mxBean.getSpecVendor();
            startTime = mxBean.getStartTime();
            upTime = mxBean.getUptime();
            inputArguments = mxBean.getInputArguments();
            bootClassPathSupported = mxBean.isBootClassPathSupported();
            bootClassPath = "";
            if (bootClassPathSupported) {
                bootClassPath = mxBean.getBootClassPath();
            }
            classPath = mxBean.getClassPath();
            libraryPath = mxBean.getLibraryPath();
            systemProperties = mxBean.getSystemProperties();
        }

        private RuntimeInfo() {
        }

        public static RuntimeInfo instance() {
            return INSTANCE;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getVmName() {
            return vmName;
        }

        public void setVmName(String vmName) {
            this.vmName = vmName;
        }

        public String getVmVersion() {
            return vmVersion;
        }

        public void setVmVersion(String vmVersion) {
            this.vmVersion = vmVersion;
        }

        public String getVmVendor() {
            return vmVendor;
        }

        public void setVmVendor(String vmVendor) {
            this.vmVendor = vmVendor;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getUpTime() {
            return upTime;
        }

        public void setUpTime(long upTime) {
            this.upTime = upTime;
        }

        public String getManagementSpecVersion() {
            return managementSpecVersion;
        }

        public void setManagementSpecVersion(String managementSpecVersion) {
            this.managementSpecVersion = managementSpecVersion;
        }

        public String getSpecName() {
            return specName;
        }

        public void setSpecName(String specName) {
            this.specName = specName;
        }

        public String getSpecVersion() {
            return specVersion;
        }

        public void setSpecVersion(String specVersion) {
            this.specVersion = specVersion;
        }

        public String getSpecVendor() {
            return specVendor;
        }

        public void setSpecVendor(String specVendor) {
            this.specVendor = specVendor;
        }

        public List<String> getInputArguments() {
            return inputArguments;
        }

        public void setInputArguments(List<String> inputArguments) {
            this.inputArguments = inputArguments;
        }

        public boolean isBootClassPathSupported() {
            return bootClassPathSupported;
        }

        public void setBootClassPathSupported(boolean bootClassPathSupported) {
            this.bootClassPathSupported = bootClassPathSupported;
        }

        public String getBootClassPath() {
            return bootClassPath;
        }

        public void setBootClassPath(String bootClassPath) {
            this.bootClassPath = bootClassPath;
        }

        public String getClassPath() {
            return classPath;
        }

        public void setClassPath(String classPath) {
            this.classPath = classPath;
        }

        public String getLibraryPath() {
            return libraryPath;
        }

        public void setLibraryPath(String libraryPath) {
            this.libraryPath = libraryPath;
        }

        public Map<String, String> getSystemProperties() {
            return systemProperties;
        }

        public void setSystemProperties(Map<String, String> systemProperties) {
            this.systemProperties = systemProperties;
        }
    }

    public static class VMOptionInfo {
        private String onError = null;
        private String onOutOfMemoryError = null;
        private String useCompressedOops = "unknown";
        private String useG1GC = "unknown";
        private String useSerialGC = "unknown";
        private long configuredInitialHeapSize = -1;
        private long configuredMaxHeapSize = -1;

        private static VMOptionInfo INSTANCE = new VMOptionInfo();

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

        public static VMOptionInfo instance() {
            return INSTANCE;
        }

        public String getOnError() {
            return onError;
        }

        public void setOnError(String onError) {
            this.onError = onError;
        }

        public String getOnOutOfMemoryError() {
            return onOutOfMemoryError;
        }

        public void setOnOutOfMemoryError(String onOutOfMemoryError) {
            this.onOutOfMemoryError = onOutOfMemoryError;
        }

        public String getUseCompressedOops() {
            return useCompressedOops;
        }

        public void setUseCompressedOops(String useCompressedOops) {
            this.useCompressedOops = useCompressedOops;
        }

        public String getUseG1GC() {
            return useG1GC;
        }

        public void setUseG1GC(String useG1GC) {
            this.useG1GC = useG1GC;
        }

        public String getUseSerialGC() {
            return useSerialGC;
        }

        public void setUseSerialGC(String useSerialGC) {
            this.useSerialGC = useSerialGC;
        }

        public long getConfiguredInitialHeapSize() {
            return configuredInitialHeapSize;
        }

        public void setConfiguredInitialHeapSize(long configuredInitialHeapSize) {
            this.configuredInitialHeapSize = configuredInitialHeapSize;
        }

        public long getConfiguredMaxHeapSize() {
            return configuredMaxHeapSize;
        }

        public void setConfiguredMaxHeapSize(long configuredMaxHeapSize) {
            this.configuredMaxHeapSize = configuredMaxHeapSize;
        }
    }

    public static class MemoryInfo {
        private long heapInit;
        private long heapMax;
        private long nonHeapInit;
        private long nonHeapMax;
        private long directMemoryMax;
        private static MemoryInfo INSTANCE = new MemoryInfo();

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

        public static MemoryInfo instance() {
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
