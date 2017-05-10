package cn.howardliu.gear.monitor.core.os;

import cn.howardliu.gear.monitor.core.Constants;
import cn.howardliu.gear.monitor.core.OperatingSystemProbe;
import cn.howardliu.gear.monitor.core.Probes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * <br>created at 17-5-4
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class OsProbe extends OperatingSystemProbe {
    private static final Logger logger = LoggerFactory.getLogger(OsProbe.class);
    private static final Method getFreePhysicalMemorySize;
    private static final Method getTotalPhysicalMemorySize;
    private static final Method getFreeSwapSpaceSize;
    private static final Method getTotalSwapSpaceSize;
    private static final Method getSystemLoadAverage;
    private static final Method getSystemCpuLoad;

    static {
        getFreePhysicalMemorySize = getMethod("getFreePhysicalMemorySize");
        getTotalPhysicalMemorySize = getMethod("getTotalPhysicalMemorySize");
        getFreeSwapSpaceSize = getMethod("getFreeSwapSpaceSize");
        getTotalSwapSpaceSize = getMethod("getTotalSwapSpaceSize");
        getSystemLoadAverage = getMethod("getSystemLoadAverage");
        getSystemCpuLoad = getMethod("getSystemCpuLoad");
    }

    public long getTotalSwapSpaceSize() {
        return Probes.getLong(getTotalSwapSpaceSize, osMxBean);
    }

    public long getFreeSwapSpaceSize() {
        return Probes.getLong(getFreeSwapSpaceSize, osMxBean);
    }

    public long getFreePhysicalMemorySize() {
        return Probes.getLong(getFreePhysicalMemorySize, osMxBean);
    }

    public long getTotalPhysicalMemorySize() {
        return Probes.getLong(getTotalPhysicalMemorySize, osMxBean);
    }

    public double getSystemCpuLoad() {
        return Probes.getDouble(getSystemCpuLoad, osMxBean);
    }

    /**
     * The system load averages as an array.
     * On Windows, this method return {@code null} now.
     * On Linux, this method return the 1, 5 and 15-minute load averages.
     * On MacOS, this method return the 1-minute load average.
     *
     * @return the avaliable system load averages or {@code null}
     */
    public double[] getSystemLoadAverage() {
        if (Constants.WINDOWS) {
            return getSystemLoadAverageOnWindows();
        } else if (Constants.LINUX) {
            return getSystemLoadAverageOnLinux();
        } else if (Constants.MAC_OS_X) {
            return getSystemLoadAverageOnMacOS();
        }
        return null;
    }

    private double[] getSystemLoadAverageOnWindows(){
        // TODO get windows' load avg
        return null;
    }

    private double[] getSystemLoadAverageOnLinux() {
        try {
            final String procLoadAvg = readProcLoadavg();
            assert procLoadAvg.matches("(\\d+\\.\\d+\\s+){3}\\d+/\\d+\\s+\\d+");
            final String[] fields = procLoadAvg.split("\\s+");
            return new double[]{
                    Double.parseDouble(fields[0]),
                    Double.parseDouble(fields[1]),
                    Double.parseDouble(fields[2])
            };
        } catch (IOException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("error reading file /proc/loadavg", e);
            }
        }
        return null;
    }

    private double[] getSystemLoadAverageOnMacOS() {
        double oneMinuteLoadAvg = Probes.getDouble(getSystemLoadAverage, osMxBean);
        assert oneMinuteLoadAvg != -1;
        return new double[]{oneMinuteLoadAvg >= 0 ? oneMinuteLoadAvg : -1, -1, -1};
    }

    @SuppressWarnings("WeakerAccess")
    protected String readProcLoadavg() throws IOException {
        return readSingleLine(FileSystems.getDefault().getPath("/proc/loadavg"));
    }

    private String readSingleLine(final Path path) throws IOException {
        final List<String> lines = Files.readAllLines(path);
        assert lines != null && lines.size() == 1;
        return lines.get(0);
    }

    public short getSystemCpuPercent() {
        return (short) (getSystemCpuLoad() * 100);
    }
}
