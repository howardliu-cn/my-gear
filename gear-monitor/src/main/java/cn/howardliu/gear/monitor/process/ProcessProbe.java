package cn.howardliu.gear.monitor.process;

import cn.howardliu.gear.monitor.OperatingSystemProbe;
import cn.howardliu.gear.monitor.Probes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * <br>created at 17-5-4
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class ProcessProbe extends OperatingSystemProbe {
    private static final Logger logger = LoggerFactory.getLogger(ProcessProbe.class);
    private static final Method getOpenFileDescriptorCount;
    private static final Method getMaxFileDescriptorCount;
    private static final Method getProcessCpuTime;
    private static final Method getProcessCpuLoad;
    private static final Method getCommittedVirtualMemorySize;

    static {
        getOpenFileDescriptorCount = getUnixMethod("getOpenFileDescriptorCount");
        getMaxFileDescriptorCount = getUnixMethod("getMaxFileDescriptorCount");
        getProcessCpuTime = getMethod("getProcessCpuTime");
        getProcessCpuLoad = getMethod("getProcessCpuLoad");
        getCommittedVirtualMemorySize = getMethod("getCommittedVirtualMemorySize");
    }


    public long getOpenFileDescriptorCount() {
        return Probes.getLong(getOpenFileDescriptorCount, osMxBean);
    }

    public long getMaxFileDescriptorCount() {
        return Probes.getLong(getMaxFileDescriptorCount, osMxBean);
    }

    /**
     * @return the CPU time (in milliseconds) used by the process on which the JVM is running, or -1 if not supported.
     */
    public long getProcessCpuTime() {
        long processCpuTIme = Probes.getLong(getProcessCpuTime, osMxBean);
        if (processCpuTIme < 0) {
            return -1;
        }
        return (processCpuTIme / 1_000_000L);
    }

    public double getProcessCpuLoad() {
        return Probes.getDouble(getProcessCpuLoad, osMxBean);
    }

    /**
     * @return the process CPU usage in percent
     */
    public short getProcessCpuPercent() {
        return (short) (getProcessCpuLoad() * 100);
    }

    public long getCommittedVirtualMemorySize() {
        return Probes.getLong(getCommittedVirtualMemorySize, osMxBean);
    }

    /**
     * @return the size (in bytes) of virtual memory that is guaranteed to be available to the running process
     */
    public long getTotalVirtualMemorySize() {
        return getCommittedVirtualMemorySize();
    }
}
