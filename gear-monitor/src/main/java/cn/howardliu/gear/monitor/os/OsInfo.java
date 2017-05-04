package cn.howardliu.gear.monitor.os;

import java.util.Set;

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
    private final int availableProcessors;
    private final long processCpuTime;
    // TODO system cpu load
    // TODO process cpu load
    private final Set<NetworkInterfaceInfo> inetAddress;

    public OsInfo() {
        availableProcessors = OSMxBeanAction.getAvailableProcessors();
        processCpuTime = OSMxBeanAction.getProcessCpuTime();
        inetAddress = NetworkInfo.getNetworkInfo();
    }

    public String getName() {
        return name;
    }

    public String getArch() {
        return arch;
    }

    public String getVersion() {
        return version;
    }

    public int getAvailableProcessors() {
        return availableProcessors;
    }

    public long getProcessCpuTime() {
        return processCpuTime;
    }

    public Set<NetworkInterfaceInfo> getInetAddress() {
        return inetAddress;
    }
}
