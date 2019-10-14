package cn.howardliu.gear.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

/**
 * <br>created at 2019/10/14
 *
 * @author liuxh
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass({
        Jackson2ObjectMapperBuilderCustomizer.class
})
public class JacksonAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(TimeZone.getTimeZone("GMT+8"));
    }
}
