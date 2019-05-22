package cn.howardliu.gear.spring.boot.autoconfigure.operator;

import cn.howardliu.gear.spring.boot.autoconfigure.cache.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br>created at 18-5-6
 *
 * @author liuxh
 * @since 0.1.0
 */
public class Operator {
    private static final Logger logger = LoggerFactory.getLogger(Operator.class);
    private String lockKey;
    private Operation operation;
    private String log;
    private CacheService cacheService;
    private int timeout = 15 * 60;

    public Operator(CacheService cacheService, String lockKey, Operation operation, String notRunLog) {
        this.cacheService = cacheService;
        this.lockKey = lockKey;
        this.operation = operation;
        this.log = notRunLog;
    }

    public Operator(CacheService cacheService, String lockKey, Operation operation, String notRunLog, int timeout) {
        this.cacheService = cacheService;
        this.lockKey = lockKey;
        this.operation = operation;
        this.log = notRunLog;
        if (timeout <= 0) {
            throw new IllegalArgumentException("超时时间不能小于等于0");
        }
        this.timeout = timeout;
    }

    public void operate() {
        if (cacheService.get(lockKey).orElse(null) != null) {
            if (((System.currentTimeMillis() - Long.valueOf(cacheService.get(lockKey).orElse("0"))) / 1000)
                    > timeout) {
                cacheService.del(lockKey);
            } else {
                logger.info(log);
                return;
            }
        }
        try {
            if (cacheService.setNX(lockKey, System.currentTimeMillis() + "")) {
                cacheService.expire(lockKey, timeout);
            } else {
                logger.info(log);
                return;
            }
            operation.execute();
        } finally {
            cacheService.del(lockKey);
        }
    }
}
