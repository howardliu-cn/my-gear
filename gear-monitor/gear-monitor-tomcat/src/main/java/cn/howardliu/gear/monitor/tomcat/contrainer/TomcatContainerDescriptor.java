package cn.howardliu.gear.monitor.tomcat.contrainer;

import cn.howardliu.gear.monitor.core.container.ConnectorInfo;
import cn.howardliu.gear.monitor.core.container.ContainerInfoDescriptor;
import cn.howardliu.gear.monitor.core.container.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
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
public class TomcatContainerDescriptor implements ContainerInfoDescriptor {
    private static final Logger logger = LoggerFactory.getLogger(TomcatContainerDescriptor.class);
    private static final ReadWriteLock connectorsLock = new ReentrantReadWriteLock();
    private static final ReadWriteLock serverInfoLock = new ReentrantReadWriteLock();
    private static List<ConnectorInfo> connectorInfoList = null;
    private static ServerInfo serverInfo = null;

    @Override
    public List<ConnectorInfo> getConnectionInfoList() throws Exception {
        connectorsLock.readLock().lock();
        if (connectorInfoList != null) {
            try {
                return connectorInfoList;
            } finally {
                connectorsLock.readLock().unlock();
            }
        }

        connectorsLock.writeLock().lock();
        try {
            MBeanServer server = MBeanServerFactory.findMBeanServer(null).get(0);
            Set<ObjectName> names = server.queryNames(new ObjectName("*:type=Connector,*"), null);
            connectorInfoList = new ArrayList<>();
            //noinspection Duplicates
            for (ObjectName name : names) {
                try {
                    String protocol = server.getAttribute(name, "protocol").toString();
                    String scheme = server.getAttribute(name, "scheme").toString();
                    Integer port = Integer.valueOf(server.getAttribute(name, "port").toString());
                    connectorInfoList.add(new ConnectorInfo().setProtocol(protocol).setScheme(scheme).setPort(port));
                } catch (Exception ignored) {
                }
            }
        } finally {
            connectorsLock.writeLock().unlock();
        }
        return connectorInfoList;
    }

    @Override
    public ServerInfo getServerInfo() throws Exception {
        serverInfoLock.readLock().lock();
        if (serverInfo != null) {
            try {
                return serverInfo;
            } finally {
                serverInfoLock.readLock().unlock();
            }
        }
        serverInfoLock.writeLock().lock();
        try {
            serverInfo = new ServerInfo()
                    .setVersion(org.apache.catalina.util.ServerInfo.getServerInfo())
                    .setBuild(org.apache.catalina.util.ServerInfo.getServerBuilt())
                    .setNumber(org.apache.catalina.util.ServerInfo.getServerNumber());
            return serverInfo;
        } finally {
            serverInfoLock.writeLock().unlock();
        }
    }
}
