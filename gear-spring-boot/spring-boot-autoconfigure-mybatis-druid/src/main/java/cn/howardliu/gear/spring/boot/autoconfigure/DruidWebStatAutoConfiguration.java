package cn.howardliu.gear.spring.boot.autoconfigure;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * <br>created at 17-5-18
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(name = "druid.stat.viewer.enabled", havingValue = "true")
@ConditionalOnClass({
        WebMvcConfigurerAdapter.class,
        FilterRegistrationBean.class,
        StatViewServlet.class,
        ServletRegistrationBean.class
})
public class DruidWebStatAutoConfiguration extends WebMvcConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(DruidWebStatAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(name = "DruidStatViewFilterRegistration")
    public FilterRegistrationBean webStatFilter() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean(new WebStatFilter());
        filterRegistration.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

    @Bean
    @ConditionalOnMissingBean
    public StatViewServlet statViewServlet() {
        return new StatViewServlet();
    }

    @Bean(name = "DruidStatViewServletRegistration")
    @ConditionalOnMissingBean(name = "DruidStatViewServletRegistration")
    public ServletRegistrationBean statViewRegistrationBean() {
        ServletRegistrationBean registration = new ServletRegistrationBean(statViewServlet(), "/druid/*");
        registration.setName("DruidStatView");
        registration.setAsyncSupported(true);
        return registration;
    }
}
