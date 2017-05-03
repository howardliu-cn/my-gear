package cn.howardliu.gear.monitor.memory;

import cn.howardliu.gear.monitor.unit.ByteSizeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
