package cn.howardliu.gear.springEx.redis;

import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <br>created at 16-12-28
 *
 * @author liuxh
 * @since 1.0.2
 */
public class LettuceConnectionFactoryBuilder implements Serializable {
    private String clusterNodes;
    private String maxRedirects;
    private String password;

    public LettuceConnectionFactoryBuilder clusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
        return this;
    }

    public LettuceConnectionFactoryBuilder maxRedirects(String maxRedirects) {
        this.maxRedirects = maxRedirects;
        return this;
    }

    public LettuceConnectionFactoryBuilder password(String password) {
        this.password = password == null ? "" : password;
        return this;
    }

    public LettuceConnectionFactory build() {
        Map<String, Object> source = new HashMap<>();
        source.put("spring.redis.cluster.nodes", this.clusterNodes);
        source.put("spring.redis.cluster.max-redirects", this.maxRedirects);
        RedisClusterConfiguration configuration = new RedisClusterConfiguration(
                new MapPropertySource("redis.properties", source));
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(configuration);
        lettuceConnectionFactory.setPassword(this.password);
        lettuceConnectionFactory.afterPropertiesSet();
        lettuceConnectionFactory.initConnection();
        return lettuceConnectionFactory;
    }
}
