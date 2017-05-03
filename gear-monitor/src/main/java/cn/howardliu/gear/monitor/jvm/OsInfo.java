package cn.howardliu.gear.monitor.jvm;

import static cn.howardliu.gear.monitor.Constants.*;

/**
 * <br>created at 17-3-9
 *
 * @author liuxh
 * @since 1.0.2
 */
public class OsInfo {
    private final String name = OS_NAME;
    private final String arch = OS_ARCH;
    private final String version = OS_VERSION;

    public String getName() {
        return name;
    }

    public String getArch() {
        return arch;
    }

    public String getVersion() {
        return version;
    }
}
