package cn.howardliu.gear.monitor.custom;

import cn.howardliu.gear.monitor.core.container.ServerInfo;
import cn.howardliu.gear.monitor.custom.connector.Connector;
import cn.howardliu.gear.monitor.custom.mbean.ConnectorMBean;
import cn.howardliu.gear.monitor.custom.modeler.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.RuntimeOperationsException;
import java.util.Set;

/**
 * <br>created at 17-5-10
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class CustomConnectorOperator {
    private static final Logger logger = LoggerFactory.getLogger(CustomConnectorOperator.class);
    private static final String domain = "Custom";
    private static final String name = domain + ":type=Connector,port=";
    private static final CustomConnectorOperator operator = new CustomConnectorOperator();
    private static ServerInfo serverInfo = null;
    private static volatile boolean registered = false;

    private CustomConnectorOperator() {
    }

    public static CustomConnectorOperator operator() {
        return operator;
    }

    public synchronized void registConnector(String scheme, String protocol, int port) throws Exception {
        assert scheme != null && !scheme.trim().isEmpty();
        assert protocol != null && !protocol.trim().isEmpty();
        assert port > 0;
        if (registered) {
            throw new RuntimeOperationsException(new RuntimeException("Repeat registration exception"));
        }
        Connector connector = new Connector(port).setScheme(scheme).setProtocol(protocol);
        ConnectorMBean connectorMBean = new ConnectorMBean();
        connectorMBean.setManagedResource(connector);
        Registry.registry().registerComponent(connectorMBean, new ObjectName(name + port));
        registered = true;
    }

    public Set<ObjectName> getConnector() throws Exception {
        return Registry.registry().getMBeanServer().queryNames(new ObjectName(name + "*"), null);
    }

    public MBeanServer getMBeanServer() throws Exception {
        return Registry.registry().getMBeanServer();
    }

    public synchronized void setServerInfo(String version, String build, String number) {
        if (serverInfo != null) {
            return;
        }
        serverInfo = new ServerInfo().setVersion(version).setBuild(build).setNumber(number);
    }

    public ServerInfo getServerInfo() {
        if (serverInfo == null) {
            logger.warn("do setServerInfo action before get");
        }
        return serverInfo;
    }
}
