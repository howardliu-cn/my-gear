package cn.howardliu.gear.springEx.redis;

import cn.howardliu.gear.id.SnowflakeIdWorker;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    private final StringRedisTemplate redisTemplate;
    private final SnowflakeIdWorker snowflakeIdWorker;
    private final long workerId;
    private final long dataCenterId;

    public SnowflakeHandler(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.workerId = next(WORKER_SEQUENCE_NAME, 32);
        this.dataCenterId = next(DATE_CENTER_SEQUENCE_NAME, 32);
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
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        long increment = entityIdCounter.getAndIncrement();
        if (increment == 0) {
            entityIdCounter.set(System.currentTimeMillis() % step);
        }
        return entityIdCounter.incrementAndGet();
    }
}
