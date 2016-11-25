package cn.howardliu.gear.commons.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.util.ArrayList;
import java.util.Set;

/**
 * <br/>created at 16-8-1
 *
 * @author liuxh
 * @since 1.0.0
 */
public class TomcatInfoUtils {
    private static final Logger logger = LoggerFactory.getLogger(TomcatInfoUtils.class);

    public static Integer getPort() throws Exception {
        MBeanServer server = MBeanServerFactory.findMBeanServer(null).get(0);
        Set<ObjectName> names = server.queryNames(new ObjectName("Catalina:type=Connector,*"), null);
        for (ObjectName name : names) {
            String protocol = server.getAttribute(name, "protocol").toString();
            String scheme = server.getAttribute(name, "scheme").toString();
            logger.debug("protocol={}, scheme={}", protocol, scheme);
            if (protocol.toLowerCase().contains("http") && scheme.toLowerCase().contains("http")) {
                return (Integer) server.getAttribute(name, "port");
            }
        }
        return 0;
    }

    private void getInfo() throws Exception {
        ArrayList<MBeanServer> mBeanServers = MBeanServerFactory.findMBeanServer(null);
        for (MBeanServer mBeanServer : mBeanServers) {
            Set<ObjectName> objectNames = mBeanServer.queryNames(null, null);
            for (ObjectName objectName : objectNames) {
                MBeanInfo mBeanInfo = mBeanServer.getMBeanInfo(objectName);
                logger.debug("objectName={}, description={}", objectName, mBeanInfo.getDescription());
                MBeanAttributeInfo[] attributes = mBeanInfo.getAttributes();
                for (MBeanAttributeInfo attribute : attributes) {
                    String name = attribute.getName();
                    String description = attribute.getDescription();
                    String type = attribute.getType();
                    logger.debug("name={}, description={}, type={}", name, description, type);
                    try {
                        Object attr = mBeanServer.getAttribute(objectName, name);
                        logger.debug("canonicalName={}, type={}, value={}", objectName.getCanonicalName(),
                                attr == null ? "null" : attr.getClass().getCanonicalName(), attr);
                    } catch (Exception ignore) {
                    }
                }
            }
        }
    }
}
