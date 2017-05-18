package cn.howardliu.gear.spring.boot.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;
import java.util.Properties;

/**
 * <br>created at 17-5-18
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
public class ViewResolverAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ViewResolverAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public ContentNegotiationManagerFactoryBean contentNegotiationManager() {
        ContentNegotiationManagerFactoryBean factory = new ContentNegotiationManagerFactoryBean();
        factory.setDefaultContentType(MediaType.APPLICATION_JSON_UTF8);
        factory.setFavorParameter(true);
        factory.setFavorPathExtension(true);
        factory.setIgnoreAcceptHeader(true);
        factory.setParameterName("format");
        Properties mediaTypes = new Properties();
        mediaTypes.put("json", MediaType.APPLICATION_JSON_UTF8_VALUE);
        mediaTypes.put("html", MediaType.TEXT_HTML_VALUE);
        mediaTypes.put("xml", MediaType.APPLICATION_XML_VALUE);
        mediaTypes.put("gif", MediaType.IMAGE_GIF_VALUE);
        mediaTypes.put("jpg", MediaType.IMAGE_JPEG_VALUE);
        mediaTypes.put("jpeg", MediaType.IMAGE_JPEG_VALUE);
        mediaTypes.put("png", MediaType.IMAGE_JPEG_VALUE);
        mediaTypes.put("ico", "image/bmp");
        mediaTypes.put("js", "text/javascript");
        factory.setMediaTypes(mediaTypes);
        return factory;
    }

    @Bean
    @ConditionalOnMissingBean
    public ContentNegotiatingViewResolver viewResolver() {
        ContentNegotiatingViewResolver viewResolver = new ContentNegotiatingViewResolver();
        viewResolver.setContentNegotiationManager(contentNegotiationManager().getObject());
        viewResolver.setDefaultViews(Collections.singletonList(new MappingJackson2JsonView()));
        return viewResolver;
    }
}
