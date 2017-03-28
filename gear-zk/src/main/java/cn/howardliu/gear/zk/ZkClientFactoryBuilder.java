package cn.howardliu.gear.zk;

import java.io.Serializable;
import java.util.Properties;

/**
 * <br>created at 16-12-28
 *
 * @author liuxh
 * @since 1.0.2
 */
public class ZkClientFactoryBuilder implements Serializable {
    private String zkAddresses;
    private String namespace;
    private Properties properties = new Properties();

    public ZkClientFactoryBuilder zkAddresses(String zkAddresses) {
        this.zkAddresses = zkAddresses;
        return this;
    }

    public ZkClientFactoryBuilder namespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public ZkClientFactoryBuilder config(Object key, Object value) {
        this.properties.put(key, value);
        return this;
    }

    public ZkClientFactoryBuilder properties(Properties properties) {
        if (properties != null) {
            this.properties.putAll(properties);
        }
        return this;
    }

    public ZkClientFactory build() {
        ZkClientFactory factory = new ZkClientFactory();
        factory.setZkAddresses(this.zkAddresses);
        factory.setNamespace(this.namespace);
        factory.setProperties(this.properties);
        return factory;
    }
}
