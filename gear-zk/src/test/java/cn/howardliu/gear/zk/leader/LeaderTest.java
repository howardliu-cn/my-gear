package cn.howardliu.gear.zk.leader;

import cn.howardliu.gear.zk.ZkClientFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * <br>created at 16-8-11
 *
 * @author liuxh
 * @since 1.0.0
 */
public class LeaderTest {
    private static final Logger logger = LoggerFactory.getLogger(LeaderTest.class);
    private CuratorFramework client;
    private String master_path = "/it-monitor/leader/omni-channel-server-listener";

    @Before
    public void before() throws Exception {
        ZkClientFactory zkClientFactory = new ZkClientFactory();
        zkClientFactory.setZkAddresses("192.168.7.137:2181,192.168.7.138:2181,192.168.7.139:2181");
        client = zkClientFactory.createClient();
    }

    @Test
    public void test() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                final Thread currentThread = Thread.currentThread();
                LeaderSelector selector = new LeaderSelector(client, master_path,
                        new LeaderSelectorListenerAdapter() {
                            @Override
                            public void takeLeadership(CuratorFramework client) throws Exception {
                                System.out.println(currentThread.getName() + "成为Master角色");
                                Thread.sleep(3000);
                                System.out.println(client.getChildren().forPath(master_path));
                                System.out.println(currentThread.getName() + "完成Master操作，释放Master权利");
                                countDownLatch.countDown();
                            }
                        });
//                selector.autoRequeue();
                selector.start();
            }).start();
        }
        countDownLatch.await();
    }
}
