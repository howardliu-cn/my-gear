package cn.howardliu.gear.spring.boot.autoconfigure.registrable;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static cn.howardliu.gear.spring.boot.autoconfigure.registrable.RegistrableProperties.REGISTRABLE_PREFIX;

/**
 * <br>created at 17-5-18
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = REGISTRABLE_PREFIX)
public class RegistrableProperties {
    public static final String REGISTRABLE_PREFIX = "spring.mvc.register";
    private String basePath = "register";
    private String preServiceName = "";

    public String getBasePath() {
        return basePath;
    }

    public RegistrableProperties setBasePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    public String getPreServiceName() {
        return preServiceName;
    }

    public RegistrableProperties setPreServiceName(String preServiceName) {
        this.preServiceName = preServiceName;
        return this;
    }
}
