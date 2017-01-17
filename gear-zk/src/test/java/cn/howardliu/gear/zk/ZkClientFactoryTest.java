package cn.howardliu.gear.zk;

import org.apache.curator.framework.CuratorFramework;
import org.junit.Test;

/**
 * <br>created at 17-1-17
 *
 * @author liuxh
 * @since 1.0.1
 */
public class ZkClientFactoryTest {
    @Test
    public void test() throws Exception {
        ZkClientFactory factory = new ZkClientFactory();
        factory.setNamespace("it-monitor");
        factory.setZkAddresses("10.6.2.78:2181,10.6.2.79:2181,10.6.2.80:2181,10.6.2.87:2181,10.6.2.88:2181");
        CuratorFramework client = factory.createClient();
        String path = "/com/wfj/monitor/sales-overview-info";
    }
}