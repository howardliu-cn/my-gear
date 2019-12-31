package cn.howardliu.gear.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Random;

/**
 * <br>created at 18-12-15
 *
 * @author liuxh
 * @since 1.0.0
 */
@Configuration
//@AutoConfigureAfter(RedisAutoConfiguration.class)
public class SnowflakeHandlerAutoConfiguration {
//    @Bean(name = "snowflakeHandler")
//    @ConditionalOnMissingBean({SnowflakeHandler.class})
//    @ConditionalOnClass({RedisConnectionFactory.class})
//    @ConditionalOnBean({RedisConnectionFactory.class})
//    public SnowflakeHandler snowflakeHandlerByRedis(RedisConnectionFactory redisConnectionFactory) {
//        return new SnowflakeHandler(redisConnectionFactory);
//    }

    @Bean(name = "snowflakeHandler")
    @ConditionalOnMissingBean({SnowflakeHandler.class})
    public SnowflakeHandler snowflakeHandler() {
        int nodeId;
        try {
            final StringBuilder sb = new StringBuilder();
            final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                final NetworkInterface networkInterface = networkInterfaces.nextElement();
                final byte[] mac = networkInterface.getHardwareAddress();
                if (mac != null) {
                    for (final byte b : mac) {
                        sb.append(String.format("%02X", b));
                    }
                }
            }
            nodeId = sb.toString().hashCode();
        } catch (Throwable ex) {
            nodeId = (new Random().nextInt());
        }
        return new SnowflakeHandler(nodeId);
    }
}
