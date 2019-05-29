package cn.howardliu.gear.spring.boot.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static cn.howardliu.gear.spring.boot.autoconfigure.XxlJobProperties.XXL_JOB_PREFIX;

/**
 * <br>created at 2019-05-25
 *
 * @author liuxh
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = XXL_JOB_PREFIX)
@Getter
@Setter
public class XxlJobProperties {
    public static final String XXL_JOB_PREFIX = "xxl.job";
    private String adminAddresses;
    private String accessToken;
    private Executor executor = new Executor();

    @Getter
    @Setter
    public class Executor {
        private String appName = "default-job-app-name";
        private String ip;
        private Integer port = 0;
        private String logPath = "./logHandler";
        private Integer logRetentionDays = 3;
    }
}
