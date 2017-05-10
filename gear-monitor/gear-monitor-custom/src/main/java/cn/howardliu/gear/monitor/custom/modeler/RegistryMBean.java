package cn.howardliu.gear.monitor.custom.modeler;

import javax.management.DynamicMBean;
import javax.management.ObjectName;
import java.util.List;

/**
 * <br>created at 17-5-10
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public interface RegistryMBean {
    /**
     * Invoke an operation on a set of mBeans
     *
     * @param mBeans    list of ObjectName
     * @param operation operation to perform. Typically "init" "start" "stop" or "destroy"
     * @param failFirst behavior in case of exceptions - if false we'll ignore
     * @throws Exception
     */
    void invoke(List<ObjectName> mBeans, String operation, boolean failFirst) throws Exception;

    /**
     * Register a bean by creating a modeler mbean and adding it to the MBeanServer.
     * <p>
     * If an mbean is already registered under this name, it'll be first unregistered.
     *
     * @param bean  object to be registered
     * @param oName name used for registration
     * @throws Exception
     */
    void registerComponent(DynamicMBean bean, String oName) throws Exception;

    /**
     * Unregister a component. We'll first check if it is registered, and mask all errors.
     * This is mostly a helper.
     *
     * @param oName name used for unregistration
     */
    void unregisterComponent(String oName);

    /**
     * Return an int ID for faster access. Will be used for notifications and for other
     * operations we want to optimize.
     *
     * @param domain namespace
     * @param name   type of the notification
     * @return an unique id for the domain:name combination
     */
    int getId(String domain, String name);

    /**
     * Reset all metadata cached by this registry. Should be called to support reloading.
     * Existing mbeans will not be affected or modified.
     * <p>
     * It will be called automatically if the registry is unregistered.
     */
    void stop();
}
