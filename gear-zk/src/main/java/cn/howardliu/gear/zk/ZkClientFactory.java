package cn.howardliu.gear.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * <br/>create at 16-5-5
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ZkClientFactory {
    private String zkAddresses;
    private RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
    private Integer connectionTimeoutMs;
    private Integer sessionTimeoutMs;
    private String namespace;

    public CuratorFramework createClient() {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        if (this.namespace != null && !this.namespace.isEmpty()) {
            builder.namespace(this.namespace);
        }
        builder.connectString(zkAddresses).retryPolicy(retryPolicy);
        if (connectionTimeoutMs != null) {
            builder.connectionTimeoutMs(connectionTimeoutMs);
        }
        if (sessionTimeoutMs != null) {
            builder.sessionTimeoutMs(sessionTimeoutMs);
        }
        CuratorFramework client = builder.build();
        client.start();
        return client;
    }

    public String getZkAddresses() {
        return zkAddresses;
    }

    public void setZkAddresses(String zkAddresses) {
        this.zkAddresses = zkAddresses;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public void setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    public Integer getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(Integer connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public Integer getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(Integer sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }
}
