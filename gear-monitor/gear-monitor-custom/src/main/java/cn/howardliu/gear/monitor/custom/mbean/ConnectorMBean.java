package cn.howardliu.gear.monitor.custom.mbean;

import cn.howardliu.gear.commons.utils.IntrospectionUtils;
import cn.howardliu.gear.monitor.custom.connector.Connector;
import cn.howardliu.gear.monitor.custom.modeler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <br>created at 17-5-9
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConnectorMBean extends BaseModelMBean {
    private static final Logger logger = LoggerFactory.getLogger(ConnectorMBean.class);
    private static final String className = ConnectorMBean.class.getName();
    private final ReadWriteLock mBeanInfoLock = new ReentrantReadWriteLock();
    private MBeanInfo mBeanInfo = null;

    @Override
    public Object getAttribute(String name)
            throws AttributeNotFoundException, MBeanException, ReflectionException {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeOperationsException(
                    new IllegalArgumentException("attribute name is null"),
                    "attribute name is null"
            );
        }
        try {
            Connector connector = (Connector) getManagedResource();
            return IntrospectionUtils.getProperty(connector, name);
        } catch (Exception e) {
            throw new MBeanException(e);
        }
    }

    @Override
    public void setAttribute(Attribute attribute)
            throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        if (attribute == null) {
            throw new RuntimeOperationsException(
                    new IllegalArgumentException("attribute is null"),
                    "attribute is null"
            );
        }
        String name = attribute.getName();
        Object value = attribute.getValue();
        if (name == null) {
            throw new RuntimeOperationsException(
                    new IllegalArgumentException("attribute name is null"),
                    "attribute name is null"
            );
        }
        try {
            Connector connector = (Connector) getManagedResource();
            IntrospectionUtils.setProperty(connector, name, String.valueOf(value));
        } catch (Exception e) {
            throw new MBeanException(e);
        }
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        mBeanInfoLock.readLock().lock();
        try {
            if (mBeanInfo != null) {
                return mBeanInfo;
            }
        } finally {
            mBeanInfoLock.readLock().unlock();
        }

        mBeanInfoLock.writeLock().lock();
        try {
            String description = "Implementation of a connector";

            AttributeInfo[] attributeInfos = ((Connector) getManagedResource()).getAttributes();
            MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[attributeInfos.length];
            for (int i = 0; i < attributeInfos.length; i++) {
                attributes[i] = attributeInfos[i].getInfo();
            }

            ConstructorInfo[] constructorInfos = ((Connector) getManagedResource()).getConstructors();
            MBeanConstructorInfo[] constructors = new MBeanConstructorInfo[constructorInfos.length];
            for (int i = 0; i < constructorInfos.length; i++) {
                constructors[i] = constructorInfos[i].getInfo();
            }

            OperationInfo[] operationInfos = ((Connector) getManagedResource()).getOperations();
            MBeanOperationInfo[] operations = new MBeanOperationInfo[operationInfos.length];
            for (int i = 0; i < operationInfos.length; i++) {
                operations[i] = operationInfos[i].getInfo();
            }

            NotificationInfo[] notificationInfos = ((Connector) getManagedResource()).getNotifications();
            MBeanNotificationInfo[] notifications = new MBeanNotificationInfo[notificationInfos.length];
            for (int i = 0; i < notificationInfos.length; i++) {
                notifications[i] = notificationInfos[i].getInfo();
            }

            mBeanInfo = new MBeanInfo(
                    className,
                    description,
                    attributes,
                    constructors,
                    operations,
                    notifications
            );
            return mBeanInfo;
        } finally {
            mBeanInfoLock.writeLock().unlock();
        }
    }
}
