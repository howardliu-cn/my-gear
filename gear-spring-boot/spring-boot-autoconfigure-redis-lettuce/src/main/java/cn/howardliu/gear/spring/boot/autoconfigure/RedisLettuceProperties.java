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

    private String nodes;
    private int maxRedirects = 3;
    private String password;
    private boolean loadRemoteCachesOnStartup = true;
    private boolean usePrefix = true;
    private int defaultExpiration = 180;

    public String getNodes() {
        return nodes;
    }

    public RedisLettuceProperties setNodes(String nodes) {
        this.nodes = nodes;
        return this;
    }

    public int getMaxRedirects() {
        return maxRedirects;
    }

    public RedisLettuceProperties setMaxRedirects(int maxRedirects) {
        this.maxRedirects = maxRedirects;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RedisLettuceProperties setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean isLoadRemoteCachesOnStartup() {
        return loadRemoteCachesOnStartup;
    }

    public RedisLettuceProperties setLoadRemoteCachesOnStartup(boolean loadRemoteCachesOnStartup) {
        this.loadRemoteCachesOnStartup = loadRemoteCachesOnStartup;
        return this;
    }

    public boolean isUsePrefix() {
        return usePrefix;
    }

    public RedisLettuceProperties setUsePrefix(boolean usePrefix) {
        this.usePrefix = usePrefix;
        return this;
    }

    public int getDefaultExpiration() {
        return defaultExpiration;
    }

    public RedisLettuceProperties setDefaultExpiration(int defaultExpiration) {
        this.defaultExpiration = defaultExpiration;
        return this;
    }
}
