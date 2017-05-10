package cn.howardliu.gear.monitor.custom.modeler;

import cn.howardliu.gear.monitor.custom.connector.Connector;
import cn.howardliu.gear.monitor.custom.mbean.ConnectorMBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.util.Set;

/**
 * <br>created at 17-5-10
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class RegistryTest {
    private String oName = "Custom:type=Connector";

    @Before
    public void setUp() throws Exception {
        Connector connector = new Connector(8080);
        ConnectorMBean connectorMBean = new ConnectorMBean();
        connectorMBean.setManagedResource(connector);
        Registry.registry().registerComponent(connectorMBean, new ObjectName(oName));
    }

    @After
    public void tearDown() throws Exception {
        Registry.registry().unregisterComponent(new ObjectName(oName));
    }

    @Test
    public void test() throws Exception {
        MBeanServer mBeanServer = Registry.registry().getMBeanServer();
        Set<ObjectName> objectNames = mBeanServer.queryNames(new ObjectName(oName), null);
        for (ObjectName objectName : objectNames) {
            String protocol = mBeanServer.getAttribute(objectName, "protocol").toString();
            String scheme = mBeanServer.getAttribute(objectName, "scheme").toString();
            String port = mBeanServer.getAttribute(objectName, "port").toString();
            System.out.println("protocol= " + protocol + ", scheme=" + scheme + ", port=" + port);
        }
    }
}