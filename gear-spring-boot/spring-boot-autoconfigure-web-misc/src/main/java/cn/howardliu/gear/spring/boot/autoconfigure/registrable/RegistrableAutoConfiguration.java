package cn.howardliu.gear.spring.boot.autoconfigure.registrable;

import cn.howardliu.gear.springEx.SpringMvcRegisterWrapper;
import cn.howardliu.gear.springEx.SpringMvcServiceScanner;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <br>created at 17-5-18
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.mvc.register", name = "basePath")
@EnableConfigurationProperties(RegistrableProperties.class)
public class RegistrableAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(RegistrableAutoConfiguration.class);
    private RegistrableProperties properties;

    public RegistrableAutoConfiguration(RegistrableProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringMvcServiceScanner springMvcServiceScanner() {
        return new SpringMvcServiceScanner();
    }

    @Bean
    public ApplicationStartupListener listener(SpringMvcRegisterWrapper springMvcRegister) {
        return new ApplicationStartupListener(springMvcRegister, properties.getSuffix());
    }

    @Bean
    @ConditionalOnBean(value = {CuratorFramework.class, SpringMvcServiceScanner.class}, name = {"zkClient"})
    @ConditionalOnMissingBean
    public SpringMvcRegisterWrapper springMvcRegister(SpringMvcServiceScanner springMvcServiceScanner,
            @Qualifier("zkClient") CuratorFramework zkClient) {
        SpringMvcRegisterWrapper wrapper = new SpringMvcRegisterWrapper();
        wrapper.setClient(zkClient);
        wrapper.setSpringMvcServiceScanner(springMvcServiceScanner);
        wrapper.setBasePath(this.properties.getBasePath());
        wrapper.setPreServiceName(this.properties.getPreServiceName());
        return wrapper;
    }
}
