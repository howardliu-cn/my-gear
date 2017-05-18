package cn.howardliu.gear.spring.boot.autoconfigure.zookeeper;

import cn.howardliu.gear.zk.ZkClientFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Properties;

/**
 * <br>created at 17-5-18
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass({ZkClientFactory.class, CuratorFramework.class})
@EnableConfigurationProperties(ZookeeperProperties.class)
public class ZookeeperAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperAutoConfiguration.class);
    private ZookeeperProperties properties;

    public ZookeeperAutoConfiguration(ZookeeperProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean(name = "zkClientFactory")
    public ZkClientFactory zkClientFactory() {
        ZkClientFactory factory = new ZkClientFactory();
        factory.setZkAddresses(StringUtils.join(this.properties.getAddresses(), ","));
        factory.setNamespace(this.properties.getNamespace());
        Properties properties = new Properties();
        for (Map.Entry<String, String> entry : this.properties.getProperties().entrySet()) {
            properties.setProperty(entry.getKey(), entry.getValue());
        }
        factory.setProperties(properties);
        return factory;
    }

    @Bean(name = "zkClient", destroyMethod = "close")
    @ConditionalOnMissingBean(name = "zkClient")
    public CuratorFramework zkClient() {
        return zkClientFactory().createClient();
    }
}
