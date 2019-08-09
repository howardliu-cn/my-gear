package cn.howardliu.gear.spring.boot.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <br>created at 17-5-18
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@ComponentScan(basePackages = {
        "cn.howardliu.gear.spring.boot.web.handler"
})
public class WebAdviceAutoConfigurer implements WebMvcConfigurer {
}
