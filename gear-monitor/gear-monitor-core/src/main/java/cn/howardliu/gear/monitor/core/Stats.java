package cn.howardliu.gear.monitor.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br>created at 17-5-5
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class Stats {
    private static final Logger logger = LoggerFactory.getLogger(Stats.class);
    protected final long timestamp;

    protected Stats(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
