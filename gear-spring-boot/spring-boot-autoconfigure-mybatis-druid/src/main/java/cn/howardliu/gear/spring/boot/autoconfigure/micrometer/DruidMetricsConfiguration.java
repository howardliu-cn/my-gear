package cn.howardliu.gear.spring.boot.autoconfigure.micrometer;

import com.alibaba.druid.pool.DruidDataSource;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <br>created at 2019/12/31
 *
 * @author liuxh
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass({DruidDataSource.class, MeterRegistry.class})
public class DruidMetricsConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(DruidMetricsConfiguration.class);
    private final MeterRegistry registry;

    public DruidMetricsConfiguration(MeterRegistry registry) {
        this.registry = registry;
    }

    @Autowired
    public void bindMetricsRegistryToDruidDataSources(Collection<DataSource> dataSources) {
        List<DruidDataSource> druidDataSources = new ArrayList<>(dataSources.size());
        for (DataSource dataSource : dataSources) {
            try {
                DruidDataSource druidDataSource = dataSource.unwrap(DruidDataSource.class);
                if (druidDataSource != null) {
                    druidDataSources.add(druidDataSource);
                }
            } catch (SQLException e) {
                logger.error("got an exception when registering DataSource to micrometer", e);
            }
        }
        new DruidCollector(druidDataSources, registry).register();
        logger.info("finish register DruidDatasource's metrics to micrometer");
    }
}
