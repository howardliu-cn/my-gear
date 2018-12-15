package cn.howardliu.gear.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * <br>created at 18-12-15
 *
 * @author liuxh
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass({
        RedisConnectionFactory.class
})
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class SnowflakeHandlerAutoConfiguration {
    @Bean(name = "snowflakeHandler")
    @ConditionalOnMissingBean({SnowflakeHandler.class})
    public SnowflakeHandler redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new SnowflakeHandler(redisConnectionFactory);
    }
}
