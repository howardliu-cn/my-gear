package cn.howardliu.gear.monitor.core.process;

import cn.howardliu.gear.monitor.core.Stats;
import cn.howardliu.gear.monitor.core.jvm.PID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br>created at 17-5-4
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class ProcessStats extends Stats {
    private static final Logger logger = LoggerFactory.getLogger(ProcessStats.class);
    private final int pid;
    private final long openFileDescriptorCount;
    private final long maxFileDescriptorCount;
    private final Cpu cpu;
    private final Mem mem;

    private ProcessStats(int pid, long timestamp, long openFileDescriptorCount, long maxFileDescriptorCount,
            Cpu cpu, Mem mem) {
        super(timestamp);
        this.pid = pid;
        this.openFileDescriptorCount = openFileDescriptorCount;
        this.maxFileDescriptorCount = maxFileDescriptorCount;
        this.cpu = cpu;
        this.mem = mem;
    }

    public static ProcessStats stats() {
        ProcessProbe probe = new ProcessProbe();
        return new ProcessStats(
                PID.getPID(),
                System.currentTimeMillis(),
                probe.getOpenFileDescriptorCount(),
                probe.getMaxFileDescriptorCount(),
                new Cpu(probe.getProcessCpuPercent(), probe.getProcessCpuTime()),
                new Mem(probe.getTotalVirtualMemorySize())
        );
    }

    public int getPid() {
        return pid;
    }

    public long getOpenFileDescriptorCount() {
        return openFileDescriptorCount;
    }

    public long getMaxFileDescriptorCount() {
        return maxFileDescriptorCount;
    }

    public Cpu getCpu() {
        return cpu;
    }

    public Mem getMem() {
        return mem;
    }

    public static class Cpu {
        private final short percent;
        private final long totalTime;

        Cpu(short percent, long totalTime) {
            this.percent = percent;
            this.totalTime = totalTime;
        }

        public short getPercent() {
            return percent;
        }

        public long getTotalTime() {
            return totalTime;
        }
    }

    public static class Mem {
        private final long totalVirtual;

        Mem(long totalVirtual) {
            this.totalVirtual = totalVirtual;
        }

        public long getTotalVirtual() {
            return totalVirtual;
        }
    }
}
