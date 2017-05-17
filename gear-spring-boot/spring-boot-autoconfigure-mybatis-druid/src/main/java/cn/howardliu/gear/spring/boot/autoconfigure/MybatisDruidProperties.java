package cn.howardliu.gear.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static cn.howardliu.gear.spring.boot.autoconfigure.MybatisDruidProperties.MYBATIS_DRUID_PREFIX;

/**
 * <br>created at 17-5-16
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = MYBATIS_DRUID_PREFIX)
public class MybatisDruidProperties {
    @SuppressWarnings("WeakerAccess")
    public static final String MYBATIS_DRUID_PREFIX = "mybatis.druid";
    private String driverClassName;
    private String jdbcUrl;
    private String username;
    private String password;
    private int initialPoolSize = 5;
    private int minPoolSize = 5;
    private int maxPoolWait = 20;
    // TODO need to complete all field for druid

    public String getDriverClassName() {
        return driverClassName;
    }

    public MybatisDruidProperties setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        return this;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public MybatisDruidProperties setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public MybatisDruidProperties setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public MybatisDruidProperties setPassword(String password) {
        this.password = password;
        return this;
    }

    public int getInitialPoolSize() {
        return initialPoolSize;
    }

    public MybatisDruidProperties setInitialPoolSize(int initialPoolSize) {
        this.initialPoolSize = initialPoolSize;
        return this;
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public MybatisDruidProperties setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
        return this;
    }

    public int getMaxPoolWait() {
        return maxPoolWait;
    }

    public MybatisDruidProperties setMaxPoolWait(int maxPoolWait) {
        this.maxPoolWait = maxPoolWait;
        return this;
    }
}
