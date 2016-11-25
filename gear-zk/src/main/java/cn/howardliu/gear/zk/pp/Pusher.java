package cn.howardliu.gear.zk.pp;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br/>created at 16-5-11
 *
 * @author liuxh
 * @since 1.0.0
 * @version %I%, %G%
 */
public class Pusher {
    private static final Logger logger = LoggerFactory.getLogger(Pusher.class);
    private CuratorFramework client;
    private String basePath;

    public Pusher(CuratorFramework client, String basePath) {
        this.client = client;
        this.basePath = basePath;
        if (!basePath.startsWith("/")) {
            this.basePath = "/" + basePath;
        } else {
            this.basePath = basePath;
        }
        try {
            if (this.client.checkExists().forPath(this.basePath) == null) {
                this.client.create().creatingParentsIfNeeded().forPath(this.basePath);
            }
        } catch (Exception e) {
            logger.error("初始化基础路径[basePath=[]]失败", this.basePath, e);
        }
    }

    public void push(String key, String value) {
        if (StringUtils.isBlank(key)) {
            logger.error("键[key={}]为空，不进行处理", key);
            return;
        }
        if (StringUtils.isBlank(value)) {
            logger.warn("值[value={}]为空，将使用空字符串作为value值", value);
            value = "";
        }
        try {
            byte[] data = value.getBytes("UTF-8");
            String nodePath = this.basePath + "/" + key;
            Stat stat = this.client.checkExists().forPath(nodePath);
            if (stat == null) {
                try {
                    this.client.create().creatingParentsIfNeeded().forPath(nodePath, data);
                } catch (Exception e) {
                    if (this.client.checkExists().forPath(nodePath) == null) {
                        throw new RuntimeException("不能创建路径:" + nodePath);
                    }
                }
            } else {
                this.client.setData().forPath(nodePath, data);
            }
        } catch (Exception e) {
            logger.error("将数据写入zk失败，[key={}, value={}]", key, value, e);
        }
    }
}
