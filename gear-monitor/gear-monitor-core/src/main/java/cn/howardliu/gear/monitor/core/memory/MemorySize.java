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

    public ByteSizeValue getTotalSize() {
        return new ByteSizeValue(totalSize);
    }

    public ByteSizeValue getUsedSize() {
        return new ByteSizeValue(usedSize);
    }

    public ByteSizeValue getFreeSize() {
        return new ByteSizeValue(freeSize);
    }
}
