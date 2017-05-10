package cn.howardliu.gear.monitor.core.memory;

/**
 * the copy of {@link java.lang.management.MemoryUsage}
 * <p>
 * <br>created at 17-5-3
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class MemoryUsage {
    private long init;
    private long used;
    private long committed;
    private long max;

    public MemoryUsage() {
    }

    private MemoryUsage(long init, long used, long committed, long max) {
        this.init = init;
        this.used = used;
        this.committed = committed;
        this.max = max;
    }

    public static MemoryUsage negative() {
        return new MemoryUsage(-1, -1, -1, -1);
    }

    public MemoryUsage clone(java.lang.management.MemoryUsage usage) {
        this.setInit(usage.getInit());
        this.setUsed(usage.getUsed());
        this.setCommitted(usage.getCommitted());
        this.setMax(usage.getMax());
        return this;
    }

    public short getUsedPercent() {
        if (max == 0) {
            return -1;
        }
        return (short) (used * 100 / max);
    }

    public long getInit() {
        return init;
    }

    private void setInit(long init) {
        this.init = init;
    }

    public long getUsed() {
        return used;
    }

    private void setUsed(long used) {
        this.used = used;
    }

    public long getCommitted() {
        return committed;
    }

    private void setCommitted(long committed) {
        this.committed = committed;
    }

    public long getMax() {
        return max;
    }

    private void setMax(long max) {
        this.max = max;
    }
}
