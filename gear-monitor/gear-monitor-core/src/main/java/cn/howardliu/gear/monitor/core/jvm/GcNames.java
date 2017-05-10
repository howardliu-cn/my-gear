package cn.howardliu.gear.monitor.core.jvm;

/**
 * <br>created at 17-2-21
 *
 * @author liuxh
 * @since 1.0.2
 */
public enum GcNames {
    YOUNG("young"),
    OLD("old"),
    SURVIVOR("survivor"),;

    public String name;

    GcNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static String getByMemoryPoolName(String poolName, String defaultName) {
        GcNames name = null;
        if ("Eden Space".equals(poolName)
                || "PS Eden Space".equals(poolName)
                || "Par Eden Space".equals(poolName)
                || "G1 Eden Space".equals(poolName)) {
            name = YOUNG;
        }
        if ("Survivor Space".equals(poolName)
                || "PS Survivor Space".equals(poolName)
                || "Par Survivor Space".equals(poolName)
                || "G1 Survivor Space".equals(poolName)) {
            name = SURVIVOR;
        }
        if ("Tenured Gen".equals(poolName)
                || "PS Old Gen".equals(poolName)
                || "CMS Old Gen".equals(poolName)
                || "G1 Old Gen".equals(poolName)) {
            name = OLD;
        }
        return name == null ? defaultName : name.getName();
    }

    public static String getByGcName(String gcName, String defaultName) {
        GcNames name = null;
        if ("Copy".equals(gcName)
                || "PS Scavenge".equals(gcName)
                || "ParNew".equals(gcName)
                || "G1 Young Generation".equals(gcName)) {
            name = YOUNG;
        }
        if ("MarkSweepCompact".equals(gcName)
                || "PS MarkSweep".equals(gcName)
                || "ConcurrentMarkSweep".equals(gcName)
                || "G1 Old Generation".equals(gcName)) {
            name = OLD;
        }
        return name == null ? defaultName : name.getName();
    }
}
