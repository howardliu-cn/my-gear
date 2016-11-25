package cn.howardliu.gear.zk.discovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * <br/>created at 16-7-3
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ServiceRegisterWrapperTest {
    private CuratorFramework client;

    @Before
    public void before() throws Exception {
        TestingServer server = new TestingServer();
        client = CuratorFrameworkFactory.newClient(server.getConnectString(), new ExponentialBackoffRetry(1000, 3));
        client.start();
    }

    @Test
    public void test() throws Exception {
        String basePath = "com/wfj/monitor/test";

        Map<String, String> servicePair = new HashMap<>();
        servicePair.put("test0", "testService/test1");
        servicePair.put("test1", "testService/test2");
        servicePair.put("test2", "testService/test3");

        ServiceRegisterWrapper register = new ServiceRegisterWrapper(client, basePath, "127.0.0.1", 8081);
        for (String key : servicePair.keySet()) {
            register.regist(new ServiceDescription(key, servicePair.get(key)));
        }

        ServiceProviderWrapper provider = new ServiceProviderWrapper(client, basePath);
        for (String key : servicePair.keySet()) {
            assertTrue(provider.provide(key).endsWith(servicePair.get(key)));
        }

        register.close();
    }
}