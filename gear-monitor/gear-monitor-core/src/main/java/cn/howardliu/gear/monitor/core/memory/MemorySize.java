package cn.howardliu.gear.monitor.core.memory;

import cn.howardliu.gear.monitor.core.unit.ByteSizeValue;

/**
 * <br>created at 17-5-3
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class MemorySize {
    private long totalSize;
    private long usedSize;
    private long freeSize;

    MemorySize(long totalSize, long usedSize, long freeSize) {
        this.totalSize = totalSize;
        this.usedSize = usedSize;
        this.freeSize = freeSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public MemorySize setTotalSize(long totalSize) {
        this.totalSize = totalSize;
        return this;
    }

    public long getUsedSize() {
        return usedSize;
    }

    public MemorySize setUsedSize(long usedSize) {
        this.usedSize = usedSize;
        return this;
    }

    public long getFreeSize() {
        return freeSize;
    }

    public MemorySize setFreeSize(long freeSize) {
        this.freeSize = freeSize;
        return this;
    }

    public ByteSizeValue getTotalSizeValue() {
        return new ByteSizeValue(totalSize);
    }

    public ByteSizeValue getUsedSizeValue() {
        return new ByteSizeValue(usedSize);
    }

    public ByteSizeValue getFreeSizeValue() {
        return new ByteSizeValue(freeSize);
    }
}
