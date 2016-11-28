package cn.howardliu.gear.zk.coordinator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * <br>created at 16-9-19
 *
 * @author liuxh
 * @since 1.0.0
 */
public class LeaderCoordinatedTask extends CoordinatedTask {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    protected LeaderSelector doTaskLeaderSelector;
    protected CountDownLatch finishLatch;

    public LeaderCoordinatedTask(CoordinatedTaskDescription taskDescription, CuratorFramework client,
            String instanceName, String namespace) {
        super(taskDescription, client, instanceName, namespace);
    }

    @Override
    public void execute() throws Exception {
        try {
            if (this.client.checkExists().forPath(this.baseTaskPath) == null) {
                this.client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(this.baseTaskPath);
            }
        } catch (KeeperException.NodeExistsException e) {
            logger.debug("任务路径[{}]已经存在，可能已经由其他任务创建", this.baseTaskPath);
        } catch (Exception e) {
            logger.error("创建任务节点{}发生异常,节点数据{}", this.baseTaskPath, taskDescription.getParamStr(), e);
            throw e;
        }
        final Thread masterThread = Thread.currentThread();
        this.doTaskLeaderSelector = new LeaderSelector(this.client, this.baseTaskPath + "/doTask",
                Executors.newCachedThreadPool(new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("TaskDaemonThread-" + thread.getId());
                        thread.setDaemon(true);
                        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                            @Override
                            public void uncaughtException(Thread t, Throwable e) {
                                taskDescription
                                        .setFailureCause(e instanceof Exception ? (Exception) e : new Exception(e));
                                masterThread.interrupt();
                            }
                        });
                        return thread;
                    }
                }), new TaskLeader());
        this.doTaskLeaderSelector.setId(this.instanceName);
        this.doTaskLeaderSelector.start();
        finishLatch = new CountDownLatch(1);
        try {
            finishLatch.await();
            logger.debug("执行结束");
        } catch (InterruptedException e) {
            logger.warn("本地执行失败");
        }
    }

    private class TaskLeader extends LeaderSelectorListenerAdapter {
        @Override
        public void takeLeadership(CuratorFramework client) throws Exception {
            boolean execute = true;
            String okFlagPath = baseTaskPath + "/okFlag";
            Stat okFlag = client.checkExists().forPath(okFlagPath);
            if (okFlag != null && Arrays.equals("ok".getBytes(), client.getData().forPath(okFlagPath))) {
                execute = false;
            }
            if (execute) {
                try {
                    taskDescription.executeTask();
                } catch (Exception e) {
                    taskDescription.setFailureCause(e);
                    taskDescription.setResultDescription("本地执行失败," + e.toString());
                }
                if (taskDescription.isSuccess()) {
                    try {
                        client
                                .create()
                                .creatingParentsIfNeeded()
                                .withMode(CreateMode.PERSISTENT)
                                .forPath(okFlagPath, "ok".getBytes());
                    } catch (Exception e) {
                        logger.warn("增加成功标识失败", e);
                    }
                }
            }
            int leftParticipants = doTaskLeaderSelector.getParticipants().size();
            if (leftParticipants <= 1) {
                client.delete().deletingChildrenIfNeeded().forPath(baseTaskPath);
            }
            finishLatch.countDown();
        }
    }
}
