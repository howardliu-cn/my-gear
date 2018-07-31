package cn.howardliu.gear.spring.boot.autoconfigure;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.time.Duration;

/**
 * <br>created at 17-5-17
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass({
        RedisTemplate.class,
        RedisCacheManager.class,
        KeyGenerator.class
})
@EnableConfigurationProperties(RedisLettuceProperties.class)
@AutoConfigureBefore(RedisAutoConfiguration.class)
@Import(RedisAutoConfiguration.class)
public class RedisLettuceAutoConfiguration extends CachingConfigurerSupport {
    private RedisConnectionFactory redisConnectionFactory;
    private RedisLettuceProperties properties;

    public RedisLettuceAutoConfiguration(RedisConnectionFactory redisConnectionFactory,
            RedisLettuceProperties properties) {
        this.redisConnectionFactory = redisConnectionFactory;
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean(name = {"redisTemplate"})
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    public RedisCacheManager cacheManager() {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(180));
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(
                        StringUtils.isBlank(this.properties.getPrefix())
                                ?
                                redisCacheConfiguration
                                :
                                redisCacheConfiguration.prefixKeysWith(this.properties.getPrefix()
                                )
                )
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    public SimpleParamsKeyGenerator keyGenerator() {
        return new SimpleParamsKeyGenerator();
    }
}
