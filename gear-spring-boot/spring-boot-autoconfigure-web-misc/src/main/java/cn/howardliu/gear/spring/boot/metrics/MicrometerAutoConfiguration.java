package cn.howardliu.gear.spring.boot.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <br>created at 2019/11/26
 *
 * @author liuxh
 * @since 1.0.0
 */
@Configuration
@AutoConfigureAfter(ProjectInfoAutoConfiguration.class)
@ConditionalOnClass({
        MeterRegistry.class,
        MeterRegistryCustomizer.class
})
public class MicrometerAutoConfiguration {
    @Bean
    @ConditionalOnBean({
            BuildProperties.class,
            GitProperties.class
    })
    public MeterRegistryCustomizer<MeterRegistry> registerCommonTagsWithGitInfoAndBuildInfo(@Value("${spring.application.name:wm-app-v1}") String applicationName,
                                                                                            @Value("${spring.cloud.client.ip-address}") String ipAddress,
                                                                                            @Value("${server.port}") String serverPort,
                                                                                            BuildProperties buildProperties,
                                                                                            GitProperties gitProperties) {
        return registry -> registry.config()
                .commonTags(
                        "application", applicationName,
                        "instance", ipAddress + ":" + serverPort,
                        "build.group", buildProperties.getGroup(),
                        "build.artifact", buildProperties.getArtifact(),
                        "git.branch", gitProperties.getBranch(),
                        "git.commit.id", gitProperties.getCommitId()
                );
    }

    @Bean
    @ConditionalOnBean(BuildProperties.class)
    @ConditionalOnMissingBean(GitProperties.class)
    public MeterRegistryCustomizer<MeterRegistry> registerCommonTagsWithBuildInfo(@Value("${spring.application.name:wm-app-v1}") String applicationName,
                                                                                  @Value("${spring.cloud.client.ip-address}") String ipAddress,
                                                                                  @Value("${server.port}") String serverPort,
                                                                                  BuildProperties buildProperties) {
        return registry -> registry.config()
                .commonTags(
                        "application", applicationName,
                        "instance", ipAddress + ":" + serverPort,
                        "build.group", buildProperties.getGroup(),
                        "build.artifact", buildProperties.getArtifact()
                );
    }

    @Bean
    @ConditionalOnBean(GitProperties.class)
    @ConditionalOnMissingBean(BuildProperties.class)
    public MeterRegistryCustomizer<MeterRegistry> registerCommonTagsWithGitInfo(@Value("${spring.application.name:wm-app-v1}") String applicationName,
                                                                                @Value("${spring.cloud.client.ip-address}") String ipAddress,
                                                                                @Value("${server.port}") String serverPort,
                                                                                GitProperties gitProperties) {
        return registry -> registry.config()
                .commonTags(
                        "application", applicationName,
                        "instance", ipAddress + ":" + serverPort,
                        "git.branch", gitProperties.getBranch(),
                        "git.commit.id", gitProperties.getCommitId()
                );
    }

    @Bean
    @ConditionalOnMissingBean({
            BuildProperties.class,
            GitProperties.class
    })
    public MeterRegistryCustomizer<MeterRegistry> registerCommonTags(@Value("${spring.application.name:wm-app-v1}") String applicationName,
                                                                     @Value("${spring.cloud.client.ip-address}") String ipAddress,
                                                                     @Value("${server.port}") String serverPort) {
        return registry -> registry.config()
                .commonTags(
                        "application", applicationName,
                        "instance", ipAddress + ":" + serverPort
                );
    }
}
