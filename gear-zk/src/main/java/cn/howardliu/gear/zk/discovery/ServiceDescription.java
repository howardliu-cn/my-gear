package cn.howardliu.gear.zk.discovery;

import java.io.Serializable;

/**
 * <br>created at 16-5-5
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ServiceDescription implements Serializable {
    private final String serviceName;
    private final String uri;
    private final String fullName;

    public ServiceDescription(String serviceName, String uri) {
        this(serviceName, uri, "");
    }

    public ServiceDescription(String serviceName, String uri, String fullName) {
        this.serviceName = serviceName;
        if (uri.startsWith("/")) {
            this.uri = uri;
        } else {
            this.uri = "/" + uri;
        }
        this.fullName = fullName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getUri() {
        return uri;
    }

    public String getFullName() {
        return fullName;
    }
}
