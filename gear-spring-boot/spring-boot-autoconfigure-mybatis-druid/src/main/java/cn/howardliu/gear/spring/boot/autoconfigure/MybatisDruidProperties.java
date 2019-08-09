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
    public static final String MYBATIS_DRUID_PREFIX = "mybatis.druid";

    private String driverClassName;
    private String jdbcUrl;
    private String username;
    private String password;
    private int initialPoolSize = 5;
    private int minPoolSize = 5;
    private int maxPoolWait = 50;
    private String defaultCharacter = "utf8";
    private boolean removeAbandoned = true;
    private int removeAbandonedTimeout = 180;
    private boolean logAbandoned = true;
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

    public String getDefaultCharacter() {
        return defaultCharacter;
    }

    public MybatisDruidProperties setDefaultCharacter(String defaultCharacter) {
        this.defaultCharacter = defaultCharacter;
        return this;
    }

    public boolean isRemoveAbandoned() {
        return removeAbandoned;
    }

    public void setRemoveAbandoned(final boolean removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
    }

    public int getRemoveAbandonedTimeout() {
        return removeAbandonedTimeout;
    }

    public void setRemoveAbandonedTimeout(final int removeAbandonedTimeout) {
        this.removeAbandonedTimeout = removeAbandonedTimeout;
    }

    public boolean isLogAbandoned() {
        return logAbandoned;
    }

    public void setLogAbandoned(final boolean logAbandoned) {
        this.logAbandoned = logAbandoned;
    }
}
