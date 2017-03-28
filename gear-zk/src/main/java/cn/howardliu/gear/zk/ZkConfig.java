package cn.howardliu.gear.zk;

/**
 * <br>created at 17-3-28
 *
 * @author liuxh
 * @since 1.0.0
 */
public enum ZkConfig {
    JuteMaxBuffer("jute.maxbuffer");

    public String key;

    ZkConfig(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
