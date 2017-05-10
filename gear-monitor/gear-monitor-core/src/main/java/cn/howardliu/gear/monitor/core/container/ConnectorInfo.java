package cn.howardliu.gear.monitor.core.container;

/**
 * <br>created at 17-5-10
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConnectorInfo {
    protected String scheme;
    protected String protocol;
    protected int port;

    public String getScheme() {
        return scheme;
    }

    public ConnectorInfo setScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public String getProtocol() {
        return protocol;
    }

    public ConnectorInfo setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ConnectorInfo setPort(int port) {
        this.port = port;
        return this;
    }
}
