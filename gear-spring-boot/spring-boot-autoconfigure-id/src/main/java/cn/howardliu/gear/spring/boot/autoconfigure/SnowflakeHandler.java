package cn.howardliu.gear.spring.boot.autoconfigure;

import cn.howardliu.gear.id.SnowflakeIdWorker;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

/**
 * <br>created at 18-12-15
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SnowflakeHandler {
    public static final String WORKER_SEQUENCE_NAME = "snowflake-worker-sequence";
    public static final String DATE_CENTER_SEQUENCE_NAME = "snowflake-data-center-sequence";
    private final RedisConnectionFactory redisConnectionFactory;
    private final SnowflakeIdWorker snowflakeIdWorker;
    private final long workerId;
    private final long dataCenterId;

    public SnowflakeHandler(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
        this.workerId = next(WORKER_SEQUENCE_NAME, 31);
        this.dataCenterId = next(DATE_CENTER_SEQUENCE_NAME, 31);
        this.snowflakeIdWorker = new SnowflakeIdWorker(workerId, dataCenterId);
    }

    public long getId() {
        return this.snowflakeIdWorker.nextId();
    }

    public long getWorkerId() {
        return workerId;
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    private long next(String key, int step) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisConnectionFactory);
        long increment = entityIdCounter.getAndIncrement();
        if (increment == 0) {
            entityIdCounter.set(System.currentTimeMillis() % step);
        }
        increment = entityIdCounter.getAndIncrement() % step;
        return increment;
    }
}
