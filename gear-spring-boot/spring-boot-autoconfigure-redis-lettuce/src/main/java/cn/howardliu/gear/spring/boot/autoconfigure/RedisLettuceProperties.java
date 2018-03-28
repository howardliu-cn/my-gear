package cn.howardliu.gear.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static cn.howardliu.gear.spring.boot.autoconfigure.RedisLettuceProperties.REDIS_LETTUCE_PREFIX;

/**
 * <br>created at 17-5-17
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = REDIS_LETTUCE_PREFIX)
public class RedisLettuceProperties {
    public static final String REDIS_LETTUCE_PREFIX = "spring.redis.cluster";
    private String prefix;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
