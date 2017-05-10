package cn.howardliu.gear.monitor.custom.modeler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.Hashtable;
import java.util.List;

/**
 * <br>created at 17-5-10
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class Registry implements RegistryMBean, MBeanRegistration {
    private static final Logger logger = LoggerFactory.getLogger(Registry.class);
    private static Registry registry = new Registry();
    private final Hashtable<String, Hashtable<String, Integer>> idDomains = new Hashtable<>();
    private final Hashtable<String, int[]> ids = new Hashtable<>();
    private MBeanServer server = null;

    private Registry() {
        super();
    }

    public static synchronized Registry registry() {
        if (registry == null) {
            registry = new Registry();
        }
        return registry;
    }

    /**
     * Factory method to create (if neccessary) and return out {@code javax.management.MBeanServer} instance.
     *
     * @return {@code javax.management.MBeanServer} instance
     */
    public synchronized MBeanServer getMBeanServer() {
        if (server == null) {
            if (MBeanServerFactory.findMBeanServer(null).size() > 0) {
                server = MBeanServerFactory.findMBeanServer(null).get(0);
                if (logger.isDebugEnabled()) {
                    logger.debug("Using existing MBeanServer " + server);
                }
            } else {
                server = ManagementFactory.getPlatformMBeanServer();
                if (logger.isDebugEnabled()) {
                    logger.debug("Creating MBeanServer " + server);
                }
            }
        }
        return server;
    }

    @Override
    public void invoke(List<ObjectName> mBeans, String operation, boolean failFirst) throws Exception {
        if (mBeans == null) {
            return;
        }
        for (ObjectName mBean : mBeans) {
            try {
                if (mBean == null) {
                    continue;
                }
                if (getMethodInfo(mBean, operation) == null) {
                    continue;
                }
                getMBeanServer().invoke(mBean, operation, new Object[]{}, new String[]{});
            } catch (Exception e) {
                if (failFirst) {
                    throw e;
                }
                logger.info("Error initializing {} {}", mBean, e.toString());
            }
        }
    }

    /**
     * find the operation info for a method
     *
     * @param oName     ObjectName instance
     * @param operation method name
     * @return the operation info for the specified operation
     */
    public MBeanOperationInfo getMethodInfo(ObjectName oName, String operation) {
        MBeanInfo info;
        try {
            info = getMBeanServer().getMBeanInfo(oName);
        } catch (Exception e) {
            logger.info("Cannot find metadata " + oName);
            return null;
        }
        MBeanOperationInfo[] infos = info.getOperations();
        for (MBeanOperationInfo operationInfo : infos) {
            if (operation.equals(operationInfo.getName())) {
                return operationInfo;
            }
        }
        return null;
    }

    @Override
    public void registerComponent(DynamicMBean bean, String oName) throws Exception {
        registerComponent(bean, new ObjectName(oName));
    }

    public void registerComponent(DynamicMBean bean, ObjectName oName) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Managed=", oName);
        }
        if (bean == null) {
            logger.error("Null component " + oName);
            return;
        }
        try {
            if (getMBeanServer().isRegistered(oName)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Unregistering existing component " + oName);
                }
                getMBeanServer().unregisterMBean(oName);
            }
            getMBeanServer().registerMBean(bean, oName);
        } catch (Exception e) {
            logger.error("Error registering " + oName, e);
            throw e;
        }
    }

    @Override
    public void unregisterComponent(String oName) {
        try {
            unregisterComponent(new ObjectName(oName));
        } catch (MalformedObjectNameException e) {
            logger.info("Error creating object name " + e);
        }
    }

    public void unregisterComponent(ObjectName objectName) {
        try {
            if (getMBeanServer().isRegistered(objectName)) {
                getMBeanServer().unregisterMBean(objectName);
            }
        } catch (Throwable t) {
            logger.error("Error unregistering mbean", t);
        }
    }

    @Override
    public synchronized int getId(String domain, String name) {
        if (domain == null) {
            domain = "";
        }
        Hashtable<String, Integer> domainTable = idDomains.get(domain);
        if (domainTable == null) {
            domainTable = new Hashtable<>();
            idDomains.put(domain, domainTable);
        }
        if (name == null) {
            name = "";
        }
        Integer i = domainTable.get(name);
        if (i != null) {
            return i;
        }
        int[] id = ids.get(domain);
        if (id == null) {
            id = new int[1];
            ids.put(domain, id);
        }
        int code = id[0]++;
        domainTable.put(name, code);
        return code;
    }

    @Override
    public void stop() {
        // do nothing
    }

    @Override
    public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
        this.server = server;
        return name;
    }

    @Override
    public void postRegister(Boolean registrationDone) {
        // do nothing
    }

    @Override
    public void preDeregister() throws Exception {
        // do nothing
    }

    @Override
    public void postDeregister() {
        // do nothing
    }
}
