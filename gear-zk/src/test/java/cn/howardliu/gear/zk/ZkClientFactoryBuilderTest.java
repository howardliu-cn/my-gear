package cn.howardliu.gear.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <br>created at 17-6-16
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class ZkClientFactoryBuilderTest {
    private CuratorFramework client;
    private String basePath = "/cn/howardliu/test/test-node-path-1";

    @Before
    public void setUp() throws Exception {
        client = new ZkClientFactoryBuilder()
                .zkAddresses("10.6.100.1:2181")
                .namespace("test-node")
                .build()
                .createClient();
    }

    @Test
    public void build() throws Exception {
        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .forPath(basePath + "/child-");
        System.err.println(new String(client.getData().forPath(basePath), "UTF-8"));
    }

    @After
    public void tearDown() throws Exception {
        if (client == null) {
            return;
        }
        client.delete().deletingChildrenIfNeeded().forPath(basePath);
        client.close();
    }
}