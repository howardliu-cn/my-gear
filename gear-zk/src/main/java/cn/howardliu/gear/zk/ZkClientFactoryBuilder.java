package cn.howardliu.gear.zk;

import java.io.Serializable;

/**
 * <br>created at 16-12-28
 *
 * @author liuxh
 * @since 1.0.2
 */
public class ZkClientFactoryBuilder implements Serializable {
    private String zkAddresses;
    private String namespace;

    public ZkClientFactoryBuilder zkAddresses(String zkAddresses) {
        this.zkAddresses = zkAddresses;
        return this;
    }

    public ZkClientFactoryBuilder namespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public ZkClientFactory build() {
        ZkClientFactory factory = new ZkClientFactory();
        factory.setZkAddresses(zkAddresses);
        factory.setNamespace(namespace);
        return factory;
    }
}
