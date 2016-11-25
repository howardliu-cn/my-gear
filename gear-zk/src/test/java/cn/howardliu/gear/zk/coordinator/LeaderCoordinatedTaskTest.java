package cn.howardliu.gear.zk.coordinator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.*;

/**
 * <br/>created at 16-9-19
 *
 * @author liuxh
 * @since 1.0.0
 */
public class LeaderCoordinatedTaskTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private CuratorFramework client;

    @Before
    public void before() throws Exception {
        TestingServer server = new TestingServer();
        client = CuratorFrameworkFactory.newClient(server.getConnectString(), new ExponentialBackoffRetry(1000, 3));
        client.start();
    }

    @Test
    public void test() throws Exception {
        int count = 10;
        final CountDownLatch asyncLock = new CountDownLatch(1);
        final CountDownLatch finishFlag = new CountDownLatch(count);
        ExecutorService executorService = Executors.newFixedThreadPool(100, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("leader-task-daemon-thread-" + t.getId());
                t.setDaemon(true);
                return t;
            }
        });
        for (int i = 0; i < count; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    CoordinatedTaskDescription<Void, Void> description = new ThisDescription();
                    CoordinatedTask task = new LeaderCoordinatedTask(description, client,
                            Thread.currentThread().getName(), "leader/coordinated/task");
                    try {
                        logger.debug("等待执行任务...");
                        asyncLock.await();
                        task.execute();
                    } catch (Exception e) {
                        logger.error("执行任务发生异常", e);
                    }
                    finishFlag.countDown();
                }
            }, null);
        }
        asyncLock.countDown();
        finishFlag.await();
    }

    private class ThisDescription extends CoordinatedTaskDescription<Void, Void> {
        public ThisDescription() {
            super("test-leader-coordinated-task", null);
        }

        @Override
        public void executeTask() throws Exception {
            logger.debug("任务开始");
            TimeUnit.SECONDS.sleep(new Random().nextInt(10));
            this.setSuccess(true);
            logger.debug("任务结束");
        }
    }
}