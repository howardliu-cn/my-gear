package cn.howardliu.gear.zk.coordinator;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br/>created at 16-5-10
 *
 * @author liuxh
 * @since 1.0.0
 */
public abstract class CoordinatedTaskDescription<P, R> {
    private static final Logger logger = LoggerFactory.getLogger(CoordinatedTaskDescription.class);
    protected final String taskName;// 任务名称
    protected final P param;// 任务参数
    protected boolean success = false;
    protected R result;
    protected Exception failureCause;
    protected String resultDescription;
    protected CoordinatedTask context;

    public CoordinatedTaskDescription(String taskName, P param) {
        this.taskName = Validate.notBlank(taskName, "任务名不能为空！");
        this.param = param;
    }

    public abstract void executeTask() throws Exception;

    public String getParamStr() {
        if (this.getParam() == null) {
            return "";
        }
        return param.toString();
    }

    public String getParamPath() {
        if (this.getParam() == null) {
            return "";
        }
        return "<" + this.getParam().toString() + ">";
    }

    public String getTaskName() {
        return taskName;
    }

    public P getParam() {
        return param;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public R getResult() {
        return result;
    }

    public void setResult(R result) {
        this.result = result;
    }

    public Exception getFailureCause() {
        return failureCause;
    }

    public void setFailureCause(Exception failureCause) {
        this.failureCause = failureCause;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public CoordinatedTask getContext() {
        return context;
    }

    public void setContext(CoordinatedTask context) {
        this.context = context;
    }
}
