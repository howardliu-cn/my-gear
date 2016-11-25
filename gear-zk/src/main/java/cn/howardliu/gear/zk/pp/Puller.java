package cn.howardliu.gear.zk.pp;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <br/>created at 16-5-11
 *
 * @author liuxh
 * @since 1.0.0
 */
public class Puller {
    private static final Logger logger = LoggerFactory.getLogger(Puller.class);
    private CuratorFramework client;
    private String basePath;

    public Puller(CuratorFramework client, String basePath) {
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

    public MutablePair<String, String> pull(String key) {
        if (StringUtils.isBlank(key)) {
            logger.error("键[key={}]为空，不进行处理", key);
            return null;
        }
        String nodePath = this.basePath + "/" + key;
        try {
            if (this.client.checkExists().forPath(nodePath) == null) {
                return null;
            }
        } catch (Exception e) {
            logger.error("检查节点[path={}]是否存在出错", nodePath, e);
        }
        try {
            return new MutablePair<>(nodePath, new String(this.client.getData().forPath(nodePath), "UTF-8"));
        } catch (UnsupportedEncodingException ignored) {
        } catch (Exception e) {
            logger.error("从路径[path={}]获取节点数据失败", nodePath, e);
        }
        return null;
    }

    public Map<String, MutablePair<String, String>> pullChildren(String key) {
        String nodePath = this.basePath;
        if (StringUtils.isNotBlank(key)) {
            nodePath = nodePath + "/" + key;
        }
        try {
            if (this.client.checkExists().forPath(nodePath) == null) {
                return null;
            }
        } catch (Exception e) {
            logger.error("检查节点[path={}]是否存在出错", nodePath, e);
        }
        List<String> children = new ArrayList<>();
        try {
            children.addAll(this.client.getChildren().forPath(nodePath));
        } catch (Exception e) {
            logger.error("从路径[path={}]获取字节点失败", nodePath, e);
        }
        Map<String, MutablePair<String, String>> map = new HashMap<>();
        for (String child : children) {
            String _key = StringUtils.isBlank(key) ? child : key + "/" + child;
            map.put(child, pull(_key));
        }
        return map;
    }
}
