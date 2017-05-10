package cn.howardliu.gear.monitor.core.container;

/**
 * <br>created at 17-5-10
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class ServerInfo {
    private String version;
    private String build;
    private String number;

    public String getVersion() {
        return version;
    }

    public ServerInfo setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getBuild() {
        return build;
    }

    public ServerInfo setBuild(String build) {
        this.build = build;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public ServerInfo setNumber(String number) {
        this.number = number;
        return this;
    }
}
