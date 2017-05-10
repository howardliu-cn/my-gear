package cn.howardliu.gear.monitor.custom.modeler;

import cn.howardliu.gear.commons.utils.IntrospectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.reflect.InvocationTargetException;

/**
 * <br>created at 17-5-10
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class BaseModelMBean implements DynamicMBean {
    private static final Logger logger = LoggerFactory.getLogger(BaseModelMBean.class);
    /**
     * The managed resource this MBean is associated with (if any).
     */
    protected Object resource = null;

    protected String resourceType = null;

    @Override
    public AttributeList getAttributes(String[] names) {
        if (names == null) {
            throw new RuntimeOperationsException(
                    new IllegalArgumentException("names[] cannot be null"),
                    "Cannot invoke a getter of " + resourceType
            );
        }
        AttributeList result = new AttributeList();
        if (names.length == 0) {
            return result;
        }
        for (String name : names) {
            try {
                result.add(new Attribute(name, getAttribute(name)));
            } catch (Exception e) {
                // Not having a particular attribute in the response is the indication of a getter problem
            }
        }
        return result;
    }

    @Override
    public AttributeList setAttributes(AttributeList attributes) {
        if (attributes == null) {
            throw new RuntimeOperationsException(
                    new IllegalArgumentException("attributes cannot be null"),
                    "Cannot invoke a setter of " + resourceType
            );
        }
        AttributeList result = new AttributeList();
        if (attributes.isEmpty()) {
            return result;
        }
        for (Object attribute : attributes) {
            Attribute attr = (Attribute) attribute;
            try {
                setAttribute(attr);
                String name = attr.getName();
                Object value = getAttribute(name);
                result.add(new Attribute(name, value));
            } catch (Exception e) {
                // ignore all exceptions
            }
        }
        return result;
    }

    @Override
    public Object invoke(String actionName, Object[] params, String[] signature)
            throws MBeanException, ReflectionException {
        if ((resource instanceof DynamicMBean)
                && !(resource instanceof BaseModelMBean)) {
            return ((DynamicMBean) resource).invoke(actionName, params, signature);
        }
        if (actionName == null) {
            throw new RuntimeOperationsException(
                    new IllegalArgumentException("Operation name cannot be null"),
                    "Cannot invoke a null operation in " + resourceType
            );
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Invoke {}", actionName);
        }
        try {
            return IntrospectionUtils.callMethodN(getManagedResource(), actionName, params, signature);
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();
            logger.error("Exception invoking method {}", actionName, t);
            if (t == null) {
                t = e;
            }
            if (t instanceof RuntimeException) {
                throw new RuntimeOperationsException((RuntimeException) t, "Exception invoking method " + actionName);
            } else if (t instanceof Error) {
                throw new RuntimeErrorException((Error) t, "Error invoking method " + actionName);
            } else {
                throw new MBeanException((Exception) t, "Exception invoking method " + actionName);
            }
        } catch (Exception e) {
            logger.error("Exception invoking method {}", actionName, e);
            throw new MBeanException(e, "Exception invoking method " + actionName);
        }
    }

    public Object getManagedResource() {
        if (resource == null) {
            throw new RuntimeOperationsException(
                    new IllegalArgumentException("Managed resource is null"),
                    "Managed resource is null"
            );
        }
        return resource;
    }

    public BaseModelMBean setManagedResource(Object resource) {
        if (resource == null) {
            throw new RuntimeOperationsException(
                    new IllegalArgumentException("Managed resource is null"),
                    "Managed resource is null"
            );
        }
        this.resource = resource;
        this.resourceType = resource.getClass().getName();
        return this;
    }

    @Override
    public String toString() {
        if (resource == null) {
            return "BaseModelMbean[" + resourceType + "]";
        }
        return resource.toString();
    }
}
