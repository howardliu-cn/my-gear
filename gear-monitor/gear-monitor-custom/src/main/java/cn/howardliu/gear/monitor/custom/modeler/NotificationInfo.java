package cn.howardliu.gear.monitor.custom.modeler;

import javax.management.MBeanNotificationInfo;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <br>created at 17-5-10
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class NotificationInfo extends FeatureInfo {
    protected String[] notificationTypes = new String[0];
    protected ReadWriteLock notificationTypesLock = new ReentrantReadWriteLock();

    public String[] getNotificationTypes() {
        notificationTypesLock.readLock().lock();
        try {
            return notificationTypes;
        } finally {
            notificationTypesLock.readLock().unlock();
        }
    }

    public NotificationInfo addNotifType(String notificationType) {
        notificationTypesLock.writeLock().lock();
        try {
            String[] results = new String[notificationTypes.length + 1];
            System.arraycopy(notificationTypes, 0, results, 0, notificationTypes.length);
            results[notificationTypes.length] = notificationType;
            notificationTypes = results;
            this.info = null;
        } finally {
            notificationTypesLock.writeLock().unlock();
        }
        return this;
    }

    public MBeanNotificationInfo getInfo() {
        if (info == null) {
            info = new MBeanNotificationInfo(getNotificationTypes(), getName(), getDescription());
        }
        return (MBeanNotificationInfo) info;
    }
}
