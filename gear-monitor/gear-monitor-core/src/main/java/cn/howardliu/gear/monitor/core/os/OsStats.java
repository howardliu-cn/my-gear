package cn.howardliu.gear.monitor.core.os;

import cn.howardliu.gear.monitor.core.Stats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br>created at 17-5-4
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class OsStats extends Stats {
    private static final Logger logger = LoggerFactory.getLogger(OsStats.class);
    private final int availableProcessors;
    private final Cpu cpu;
    private final Mem mem;
    private final Mem swap;

    private OsStats(long timestamp, int availableProcessors, Cpu cpu, Mem mem, Mem swap) {
        super(timestamp);
        this.availableProcessors = availableProcessors;
        this.cpu = cpu;
        this.mem = mem;
        this.swap = swap;
    }

    public static OsStats stats() {
        final OsProbe osProbe = new OsProbe();
        return new OsStats(
                System.currentTimeMillis(),
                Runtime.getRuntime().availableProcessors(),
                new Cpu(osProbe.getSystemCpuPercent(), osProbe.getSystemLoadAverage()),
                new Mem(osProbe.getTotalPhysicalMemorySize(), osProbe.getFreePhysicalMemorySize()),
                new Mem(osProbe.getTotalSwapSpaceSize(), osProbe.getFreeSwapSpaceSize())
        );
    }

    public static short percentage(long used, long max) {
        return max <= 0 ? 0 : (short) (Math.round((100d * used) / max));
    }

    public int getAvailableProcessors() {
        return availableProcessors;
    }

    public Cpu getCpu() {
        return cpu;
    }

    public Mem getMem() {
        return mem;
    }

    public Mem getSwap() {
        return swap;
    }

    public static class Cpu {
        private final short percent;
        private final double[] loadAverages;

        Cpu(short percent, double[] loadAverages) {
            this.percent = percent;
            this.loadAverages = loadAverages;
        }

        public short getPercent() {
            return percent;
        }

        public double[] getLoadAverages() {
            return loadAverages;
        }
    }

    public static class Mem {
        private final long total;
        private final long free;
        private final long used;

        Mem(long total, long free) {
            this.total = total;
            this.free = free;
            this.used = total - free;
        }

        public short getUsedPercent() {
            return percentage(this.used, this.total);
        }

        public short getFreePercent() {
            return percentage(this.free, this.total);
        }

        public long getTotal() {
            return total;
        }

        public long getFree() {
            return free;
        }

        public long getUsed() {
            return used;
        }
    }
}
