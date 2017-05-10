package cn.howardliu.gear.monitor.custom.contrainer;

import cn.howardliu.gear.monitor.core.container.ConnectorInfo;
import cn.howardliu.gear.monitor.core.container.ContainerInfoDescriptor;
import cn.howardliu.gear.monitor.core.container.ServerInfo;
import cn.howardliu.gear.monitor.custom.CustomConnectorOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <br>created at 17-5-10
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class CustomContainerDescriptor implements ContainerInfoDescriptor {
    private static final Logger logger = LoggerFactory.getLogger(CustomContainerDescriptor.class);
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private static List<ConnectorInfo> connectorInfoList = null;

    @Override
    public List<ConnectorInfo> getConnectionInfoList() throws Exception {
        lock.readLock().lock();
        if (connectorInfoList != null) {
            return connectorInfoList;
        }
        lock.readLock().unlock();
        lock.writeLock().lock();
        try {
            CustomConnectorOperator operator = CustomConnectorOperator.operator();
            MBeanServer mBeanServer = operator.getMBeanServer();
            Set<ObjectName> objectNames = operator.getConnector();
            connectorInfoList = new ArrayList<>();
            //noinspection Duplicates
            for (ObjectName objectName : objectNames) {
                try {
                    String protocol = mBeanServer.getAttribute(objectName, "protocol").toString();
                    String scheme = mBeanServer.getAttribute(objectName, "scheme").toString();
                    Integer port = Integer.valueOf(mBeanServer.getAttribute(objectName, "port").toString());
                    connectorInfoList.add(new ConnectorInfo().setProtocol(protocol).setScheme(scheme).setPort(port));
                } catch (Exception ignore) {
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
        return connectorInfoList;
    }

    @Override
    public ServerInfo getServerInfo() throws Exception {
        return null;
    }
}
