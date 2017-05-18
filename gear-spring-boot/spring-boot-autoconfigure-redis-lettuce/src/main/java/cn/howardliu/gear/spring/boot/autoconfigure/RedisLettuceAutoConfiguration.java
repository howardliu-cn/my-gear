package cn.howardliu.gear.spring.boot.autoconfigure;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.util.HashMap;
import java.util.Map;

import static cn.howardliu.gear.spring.boot.autoconfigure.RedisLettuceProperties.REDIS_LETTUCE_PREFIX;

/**
 * <br>created at 17-5-17
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass({
        RedisClusterConfiguration.class,
        LettuceConnectionFactory.class,
        StringRedisTemplate.class,
        RedisCacheManager.class
})
@ConditionalOnProperty(prefix = REDIS_LETTUCE_PREFIX, name = "nodes")
@EnableConfigurationProperties(RedisLettuceProperties.class)
@AutoConfigureBefore(RedisAutoConfiguration.class)
@EnableCaching
public class RedisLettuceAutoConfiguration extends CachingConfigurerSupport {
    private static final Logger logger = LoggerFactory.getLogger(RedisLettuceAutoConfiguration.class);

    private RedisLettuceProperties properties;

    public RedisLettuceAutoConfiguration(RedisLettuceProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisClusterConfiguration clusterConfig() {
        Map<String, Object> map = new HashMap<>();
        map.put("spring.redis.cluster.nodes", this.properties.getNodes());
        map.put("spring.redis.cluster.max-redirects", this.properties.getMaxRedirects());
        return new RedisClusterConfiguration(new MapPropertySource("redis.properties", map));
    }

    @Bean(destroyMethod = "destroy")
    @ConditionalOnMissingBean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(clusterConfig());
        if (StringUtils.isNotBlank(this.properties.getPassword())) {
            factory.setPassword(this.properties.getPassword());
        }
        return factory;
    }

    @Bean
    @ConditionalOnMissingBean
    public StringRedisTemplate redisTemplate() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate(lettuceConnectionFactory());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    public RedisCacheManager cacheManager() {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate());
        cacheManager.setLoadRemoteCachesOnStartup(this.properties.isLoadRemoteCachesOnStartup());
        cacheManager.setUsePrefix(this.properties.isUsePrefix());
        cacheManager.setDefaultExpiration(this.properties.getDefaultExpiration());
        return cacheManager;
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    public SimpleParamsKeyGenerator keyGenerator() {
        return new SimpleParamsKeyGenerator();
    }
}
