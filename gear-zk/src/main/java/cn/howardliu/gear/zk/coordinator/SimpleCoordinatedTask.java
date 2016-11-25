package cn.howardliu.gear.zk.coordinator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * <br/>created at 16-5-10
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SimpleCoordinatedTask extends CoordinatedTask {
    private static final Logger logger = LoggerFactory.getLogger(SimpleCoordinatedTask.class);

    public SimpleCoordinatedTask(CoordinatedTaskDescription taskDescription, CuratorFramework client,
            String instanceName, String namespace) {
        super(taskDescription, client, instanceName, namespace);
    }

    public void execute() throws Exception {
        boolean execute = false;
        // 1. 创建任务基础路径
        try {
            this.client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(this.baseTaskPath, taskDescription.getParamStr().getBytes());
            execute = true;
        } catch (KeeperException.NodeExistsException e) {
            logger.debug("任务路径[{}]已经存在，有其他节点在执行任务", this.baseTaskPath);
        } catch (Exception e) {
            logger.error("创建任务节点{}发生异常,节点数据{}", this.baseTaskPath, taskDescription.getParamStr(), e);
            throw e;
        }
        if (execute) {
            for (int i = 0; i < retryTime; i++) {
                try {
                    taskDescription.executeTask();
                    success = true;
                    break;
                } catch (Exception e) {
                    logger.error("第{}次任务执行失败", i, e);
                    error = e;
                }
                if (i == retryTime - 1) {
                    logger.error("重试{}次，任务失败", i, error);
                }
            }
            if (success) {
                if (effectiveTime < 0) {
                    removeTaskPath();
                } else {
                    new Timer(this.baseTaskPath + "-timer", true).schedule(new TimerTask() {
                        @Override
                        public void run() {
                            removeTaskPath();
                        }
                    }, effectiveTime);
                }
            } else {
                removeTaskPath();
            }
        }
    }
}
