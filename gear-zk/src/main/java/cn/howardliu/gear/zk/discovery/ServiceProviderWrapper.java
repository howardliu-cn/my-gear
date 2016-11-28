package cn.howardliu.gear.zk.discovery;

import com.google.common.collect.Maps;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.*;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.apache.curator.x.discovery.strategies.RoundRobinStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

/**
 * <br>created at 16-5-5
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ServiceProviderWrapper implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderWrapper.class);
    private final Map<String, ServiceProvider<InstanceDetails>> providers = Maps.newHashMap();
    private ProviderStrategy<InstanceDetails> providerStrategy = new RoundRobinStrategy<>();
    private final ServiceDiscovery<InstanceDetails> serviceDiscovery;
    private static JsonInstanceSerializer<InstanceDetails> serializer = new JsonInstanceSerializer<>(
            InstanceDetails.class);

    public ServiceProviderWrapper(CuratorFramework client, String basePath) throws Exception {
        this.serviceDiscovery = ServiceDiscoveryBuilder
                .builder(InstanceDetails.class)
                .basePath(basePath)
                .client(client)
                .serializer(serializer)
                .build();
        serviceDiscovery.start();
    }

    public String provide(String serviceName) throws Exception {
        synchronized (providers) {
            ServiceProvider<InstanceDetails> provider = providers.get(serviceName);
            if (provider == null) {
                provider = serviceDiscovery
                        .serviceProviderBuilder()
                        .serviceName(serviceName)
                        .providerStrategy(this.providerStrategy)
                        .build();
                providers.put(serviceName, provider);
                provider.start();
            }
            ServiceInstance<InstanceDetails> instance = provider.getInstance();
            if (instance == null) {
                return null;
            }
            return instance.buildUriSpec();
        }
    }

    @Override
    public void close() throws IOException {
        for (ServiceProvider<InstanceDetails> provider : providers.values()) {
            CloseableUtils.closeQuietly(provider);
        }
    }

    public void setProviderStrategy(ProviderStrategy<InstanceDetails> providerStrategy) {
        this.providerStrategy = providerStrategy;
    }
}
