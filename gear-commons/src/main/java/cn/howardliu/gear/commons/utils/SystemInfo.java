package cn.howardliu.gear.commons.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * <br>created at 2020/5/5
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SystemInfo {
    private final static double KB = 1024;
    private final static double MB = 1024 * KB;
    private final static double GB = 1024 * MB;
    private final static DecimalFormat decimalFormat = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ROOT));

    public static Map<String, Object> getSystemInfo() {
        final Map<String, Object> info = new HashMap<>();

        final OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();

        info.put("name", os.getName());
        info.put("version", os.getVersion());
        info.put("arch", os.getArch());
        info.put("systemLoadAverage", os.getSystemLoadAverage());

        // com.sun.management.OperatingSystemMXBean
        info.put("committedVirtualMemorySize", addGetterIfAvaliable(os, "committedVirtualMemorySize"));
        info.put("freePhysicalMemorySize", addGetterIfAvaliable(os, "freePhysicalMemorySize"));
        info.put("freeSwapSpaceSize", addGetterIfAvaliable(os, "freeSwapSpaceSize"));
        info.put("processCpuTime", addGetterIfAvaliable(os, "processCpuTime"));
        info.put("totalPhysicalMemorySize", addGetterIfAvaliable(os, "totalPhysicalMemorySize"));
        info.put("totalSwapSpaceSize", addGetterIfAvaliable(os, "totalSwapSpaceSize"));

        // com.sun.management.UnixOperatingSystemMXBean
        info.put("openFileDescriptorCount", addGetterIfAvaliable(os, "openFileDescriptorCount"));
        info.put("maxFileDescriptorCount", addGetterIfAvaliable(os, "maxFileDescriptorCount"));

        try {
            if (!os.getName().toLowerCase(Locale.ROOT).startsWith("windows")) {
                info.put("uname", Processor.execute("uname -a"));
                info.put("uptime", Processor.execute("uptime"));
            }
        } catch (Exception ignored) {
        }
        return info;
    }

    public static Map<String, Object> getJvmInfo() {
        final Map<String, Object> jvm = new HashMap<>();

        final String javaVersion = System.getProperty("java.specification.version", "unknown");
        final String javaVendor = System.getProperty("java.specification.vendor", "unknown");
        final String javaName = System.getProperty("java.specification.name", "unknown");
        final String jreVersion = System.getProperty("java.version", "unknown");
        final String jreVendor = System.getProperty("java.vendor", "unknown");
        final String vmVersion = System.getProperty("java.vm.version", "unknown");
        final String vmVendor = System.getProperty("java.vm.vendor", "unknown");
        final String vmName = System.getProperty("java.vm.name", "unknown");

        jvm.put("version", jreVersion + " " + vmVersion);
        jvm.put("name", jreVendor + " " + vmName);

        final Map<String, String> specMap = new HashMap<>();
        specMap.put("vendor", javaVendor);
        specMap.put("name", javaName);
        specMap.put("version", javaVersion);
        jvm.put("spec", specMap);

        final Map<String, String> jreMap = new HashMap<>();
        jreMap.put("vendor", jreVendor);
        jreMap.put("version", jreVersion);
        jvm.put("jre", jreMap);

        final Map<String, String> vmMap = new HashMap<>();
        vmMap.put("vendor", vmVendor);
        vmMap.put("name", vmName);
        vmMap.put("version", vmVersion);
        jvm.put("vm", vmMap);

        final Runtime runtime = Runtime.getRuntime();
        jvm.put("processors", runtime.availableProcessors());

        final long freeMemory = runtime.freeMemory();
        final long maxMemory = runtime.maxMemory();
        final long totalMemory = runtime.totalMemory();
        final long usedMemory = totalMemory - freeMemory;
        final double percentUsed = usedMemory * 100.0 / maxMemory;

        Map<String, Object> memMap = new HashMap<>();
        memMap.put("free", humanReadableUnits(freeMemory, decimalFormat));
        memMap.put("total", humanReadableUnits(totalMemory, decimalFormat));
        memMap.put("max", humanReadableUnits(maxMemory, decimalFormat));
        memMap.put("used", humanReadableUnits(usedMemory, decimalFormat) + "(" + decimalFormat.format(percentUsed) + "%)");

        Map<String, Number> raw = new HashMap<>();
        raw.put("free", freeMemory);
        raw.put("used", usedMemory);
        raw.put("used%", percentUsed);
        raw.put("total", totalMemory);
        raw.put("max", maxMemory);
        memMap.put("raw", raw);

        jvm.put("memory", memMap);

        final RuntimeMXBean mx = ManagementFactory.getRuntimeMXBean();

        Map<String, Object> jmx = new HashMap<>();
        jmx.put("bootclasspath", mx.getBootClassPath());
        jmx.put("classpath", mx.getClassPath());
        jmx.put("commandLineArgs", mx.getInputArguments());
        jmx.put("startTime", mx.getStartTime());
        jmx.put("upTimeMS", mx.getUptime());

        jvm.put("jmx", jmx);

        return jvm;
    }

    private static String humanReadableUnits(final long bytes, final DecimalFormat df) {
        final double gbBytes = bytes / GB;
        if ((long) gbBytes > 0) {
            return df.format(gbBytes) + "GB";
        }
        final double mbBytes = bytes / MB;
        if ((long) mbBytes > 0) {
            return df.format(mbBytes) + "MB";
        }
        final double kbBytes = bytes / KB;
        if ((long) kbBytes > 0) {
            return df.format(kbBytes) + "KB";
        }
        return df.format(bytes) + "B";
    }

    private static Object addGetterIfAvaliable(final Object obj, String getter) {
        try {
            final String n = Character.toUpperCase(getter.charAt(0)) + getter.substring(1);
            final Method m = obj.getClass().getMethod("get" + n);
            return m.invoke(obj);
        } catch (Exception ignored) {
            return "";
        }
    }
}
