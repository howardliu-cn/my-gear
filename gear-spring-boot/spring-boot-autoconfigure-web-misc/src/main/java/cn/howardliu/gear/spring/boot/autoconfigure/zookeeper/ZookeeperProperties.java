package cn.howardliu.gear.spring.boot.autoconfigure.zookeeper;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.*;

import static cn.howardliu.gear.spring.boot.autoconfigure.zookeeper.ZookeeperProperties.ZOOKEEPER_PREFIX;

/**
 * <br>created at 17-5-18
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = ZOOKEEPER_PREFIX)
public class ZookeeperProperties {
    public static final String ZOOKEEPER_PREFIX = "spring.zookeeper";

    private List<String> addresses = new ArrayList<>(Collections.singletonList("localhost:2181"));
    private String namespace;
    private Map<String, String> properties = new HashMap<>();

    public List<String> getAddresses() {
        return addresses;
    }

    public ZookeeperProperties setAddresses(List<String> addresses) {
        this.addresses = addresses;
        return this;
    }

    public String getNamespace() {
        return namespace;
    }

    public ZookeeperProperties setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public ZookeeperProperties setProperties(Map<String, String> properties) {
        this.properties = properties;
        return this;
    }
}
