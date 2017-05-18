package cn.howardliu.gear.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeperMain;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * <br>created at 17-1-17
 *
 * @author liuxh
 * @since 1.0.1
 */
@Ignore
public class ZkClientFactoryTest {
    @Test
    public void test() throws Exception {
        ZkClientFactory factory = new ZkClientFactory();
        factory.setNamespace("it-monitor");
        factory.setZkAddresses("10.6.2.78:2181,10.6.2.79:2181,10.6.2.80:2181,10.6.2.87:2181,10.6.2.88:2181");
        CuratorFramework client = factory.createClient();
        String path = "/com/wfj/monitor/sales-overview-info";
    }

    @Test
    public void test2() throws Exception {
        ZooKeeper zk = new ZooKeeper("10.6.2.49:2181", 1000, event -> {
            ZooKeeperMain.printMessage("WATCHER::");
            ZooKeeperMain.printMessage(event.toString());
        });
        zk.create("/server", "this is a test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);
        TimeUnit.SECONDS.sleep(60);
    }
}