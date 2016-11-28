package cn.howardliu.gear.springEx;

import cn.howardliu.gear.zk.discovery.InstanceDetails;
import cn.howardliu.gear.zk.discovery.ServiceProviderWrapper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ProviderStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * <br>created at 16-5-6
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SpringMvcProvider implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(SpringMvcProvider.class);
    private final ServiceProviderWrapper serviceProviderWrapper;
    private ProviderStrategy<InstanceDetails> providerStrategy;
    private String preServiceName = "";

    public SpringMvcProvider(CuratorFramework client, String basePath) throws Exception {
        serviceProviderWrapper = new ServiceProviderWrapper(client, basePath);
    }

    public SpringMvcProvider(CuratorFramework client, String basePath,
            ProviderStrategy<InstanceDetails> providerStrategy) throws Exception {
        this(client, basePath);
        serviceProviderWrapper.setProviderStrategy(providerStrategy);
    }

    public String provide(String serviceName) throws Exception {
        String trueServiceName = preServiceName + "-" + serviceName;
        trueServiceName = trueServiceName.replaceAll("--+", "-").replaceAll("-$", "").replaceAll("^-", "");
        return serviceProviderWrapper.provide(trueServiceName);
    }

    @Override
    public void close() throws IOException {
        serviceProviderWrapper.close();
    }

    public void setPreServiceName(String preServiceName) {
        if (preServiceName == null) {
            logger.warn("服务名前缀不能为null，将使用默认的空字符串");
            this.preServiceName = "";
        } else {
            this.preServiceName = preServiceName.trim();
        }
    }
}
