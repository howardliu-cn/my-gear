package cn.howardliu.gear.zk.discovery;

import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.*;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * <br>created at 16-5-5
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ServiceRegisterWrapper implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(ServiceRegisterWrapper.class);
    private final List<ServiceInstance<InstanceDetails>> serviceInstances = Lists.newArrayList();
    private ServiceDiscovery<InstanceDetails> serviceDiscovery;
    private final static String baseAddress = "{scheme}://{address}:{port}";
    private final String address;
    private final int port;
    private static JsonInstanceSerializer<InstanceDetails> serializer = new JsonInstanceSerializer<>(
            InstanceDetails.class);

    public ServiceRegisterWrapper(CuratorFramework client, String basePath, String address) throws Exception {
        this(client, address, basePath, 80);
    }

    public ServiceRegisterWrapper(CuratorFramework client, String basePath, String address, int port) throws Exception {
        this.address = address;
        this.port = port;

        serviceDiscovery = ServiceDiscoveryBuilder.builder(InstanceDetails.class)
                .client(client)
                .basePath(basePath)
                .serializer(serializer)
                .build();
        serviceDiscovery.start();
    }

    public synchronized void regist(ServiceDescription serviceDescription) throws Exception {
        UriSpec uriSpec = new UriSpec(baseAddress + serviceDescription.getUri());
        ServiceInstance<InstanceDetails> thisInstance = ServiceInstance.<InstanceDetails>builder()
                .name(serviceDescription.getServiceName())
                .payload(new InstanceDetails())
                .address(address)
                .port(port)
                .serviceType(ServiceType.DYNAMIC)
                .uriSpec(uriSpec)
                .build();
        if (logger.isDebugEnabled()) {
            logger.debug("{} = {}", thisInstance.getName(), thisInstance.buildUriSpec());
        }

        serviceDiscovery.registerService(thisInstance);
        serviceInstances.add(thisInstance);
    }

    public synchronized void refresh(String description) throws Exception {
        for (ServiceInstance<InstanceDetails> serviceInstance : serviceInstances) {
            if (logger.isDebugEnabled()) {
                logger.debug("{} = {}", serviceInstance.getName(), serviceInstance.buildUriSpec());
            }
            InstanceDetails payload = serviceInstance.getPayload();
            payload.setDescription(description);
            try {
                serviceDiscovery.updateService(serviceInstance);
            } catch (KeeperException.NoNodeException e) {
                logger.warn("找不到注册节点，重新注册，异常为：{}", e.toString());
                serviceDiscovery.registerService(serviceInstance);
            }
        }
    }

    @Override
    public void close() throws IOException {
        for (ServiceInstance<InstanceDetails> serviceInstance : this.serviceInstances) {
            try {
                serviceDiscovery.unregisterService(serviceInstance);
            } catch (Exception ignored) {
            }
        }
        CloseableUtils.closeQuietly(serviceDiscovery);
    }
}
