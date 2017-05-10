package cn.howardliu.gear.monitor.custom.connector;

import cn.howardliu.gear.monitor.custom.modeler.AttributeInfo;
import cn.howardliu.gear.monitor.custom.modeler.ConstructorInfo;
import cn.howardliu.gear.monitor.custom.modeler.NotificationInfo;
import cn.howardliu.gear.monitor.custom.modeler.OperationInfo;

/**
 * <br>created at 17-5-9
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class Connector {
    /**
     * the request scheme that will be set on all requests received through this connector.
     */
    private String scheme = "http";
    /**
     * the protocol in use.
     */
    private String protocol = "HTTP/1.1";
    /**
     * the port number on which we listen for requests.
     */
    private int port = -1;

    private ConstructorInfo[] constructorInfos = null;
    private AttributeInfo[] attributeInfos = null;
    private OperationInfo[] operationInfos = new OperationInfo[0];
    private NotificationInfo[] notificationInfos = new NotificationInfo[0];

    public Connector(int port) {
        this.port = port;
    }

    public String getScheme() {
        return scheme;
    }

    public Connector setScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public String getProtocol() {
        return protocol;
    }

    public Connector setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public int getPort() {
        return port;
    }

    public Connector setPort(int port) {
        this.port = port;
        return this;
    }

    public ConstructorInfo[] getConstructors() {
        if (constructorInfos == null) {
            constructorInfos = new ConstructorInfo[1];
            constructorInfos[0] = (ConstructorInfo) new ConstructorInfo()
                    .setDescription("No-parameter constructor");
        }
        return constructorInfos;
    }

    public AttributeInfo[] getAttributes() {
        if (attributeInfos == null) {
            attributeInfos = new AttributeInfo[3];
            attributeInfos[0] = (AttributeInfo) new AttributeInfo()
                    .setReadable(true)
                    .setWriteable(false)
                    .setIs(false)
                    .setName("scheme")
                    .setDescription("Protocol name for this Connector (http, https)")
                    .setType("java.lang.String");

            attributeInfos[1] = (AttributeInfo) new AttributeInfo()
                    .setReadable(true)
                    .setWriteable(false)
                    .setIs(false)
                    .setName("protocol")
                    .setDescription("protocol in use")
                    .setType("java.lang.String");

            attributeInfos[2] = (AttributeInfo) new AttributeInfo()
                    .setReadable(true)
                    .setWriteable(false)
                    .setIs(false)
                    .setName("port")
                    .setDescription("The port number on which this connector is configured to listen for requests.")
                    .setType("int");
        }
        return attributeInfos;
    }

    public OperationInfo[] getOperations() {
        return operationInfos;
    }

    public NotificationInfo[] getNotifications() {
        return notificationInfos;
    }
}
