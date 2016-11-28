package cn.howardliu.gear.zk.coordinator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br>created at 16-9-19
 *
 * @author liuxh
 * @since 1.0.0
 */
public abstract class CoordinatedTask {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final CoordinatedTaskDescription taskDescription;
    protected final CuratorFramework client;
    protected final String namespace;
    protected final String instanceName;
    protected final String baseTaskPath;
    protected int retryTime = 3;
    protected int effectiveTime = -1;
    protected boolean success = false;
    protected Throwable error;

    public CoordinatedTask(CoordinatedTaskDescription taskDescription, CuratorFramework client,
            String instanceName, String namespace) {
        this.taskDescription = Validate.notNull(taskDescription, "任务描述不能为 null！");
        this.taskDescription.setContext(this);

        this.client = Validate.notNull(client, "ZooKeeper客户端不能为 null！");
        if (this.client.getState() == CuratorFrameworkState.STOPPED) {
            throw new IllegalArgumentException("ZooKeeper客户端已关闭!");
        } else if (this.client.getState() == CuratorFrameworkState.LATENT) {
            this.client.start();
        }

        this.namespace = Validate.notBlank(namespace, "命名空间不能为空!");
        this.instanceName = Validate.notBlank(instanceName, "当前实例名不能为空");
        this.baseTaskPath = this.getBaseTaskPath();
    }

    public abstract void execute() throws Exception;

    protected void removeTaskPath() {
        try {
            Stat stat = this.client.checkExists().forPath(this.baseTaskPath);
            if (stat != null) {
                this.client.delete().guaranteed().deletingChildrenIfNeeded().forPath(this.baseTaskPath);
            }
        } catch (Exception e) {
            logger.error("任务节点{}删除失败", this.baseTaskPath, e);
        }
    }

    private String getBaseTaskPath() {
        String paramPath = this.taskDescription.getParamPath();
        return "/" + this.namespace +
                "/" + this.taskDescription.getTaskName() +
                (StringUtils.isBlank(paramPath) ? "" : ("/" + paramPath));
    }

    public CoordinatedTask setRetryTime(int retryTime) {
        if (retryTime <= 0) {
            logger.warn("任务失败重试次数不能小于1，将使用默认重试次数{}", retryTime);
        } else {
            this.retryTime = retryTime;
        }
        return this;
    }

    public CoordinatedTask setEffectiveTime(int effectiveTime) {
        if (effectiveTime <= 0) {
            this.effectiveTime = -1;
        } else {
            this.effectiveTime = effectiveTime;
        }
        return this;
    }
}
