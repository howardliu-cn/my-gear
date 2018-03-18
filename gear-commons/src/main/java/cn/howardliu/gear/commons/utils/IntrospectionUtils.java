package cn.howardliu.gear.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * <br>created at 17-5-9
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public final class IntrospectionUtils {
    private static final Logger logger = LoggerFactory.getLogger(IntrospectionUtils.class);
    private static final Map<Class<?>, Method[]> objectMethods = Collections.synchronizedMap(new HashMap<Class<?>, Method[]>());

    /**
     * Find a method with the right name if found, call the method (if param is
     * int or boolean we'll convert value to the right type before) - that means
     * you can have setDebug(1).
     *
     * @param o     the object to set a property on
     * @param name  the property name
     * @param value the property value
     * @return {@code true} if operation was successfully
     */
    public static boolean setProperty(Object o, String name, String value) {
        return setProperty(o, name, value, true);
    }

    public static boolean setProperty(Object o, String name, String value, boolean invokeSetProperty) {
        if (logger.isDebugEnabled()) {
            logger.debug("IntrospectionUtils: setProperty({}, {}, {}, {})", o, name, value, invokeSetProperty);
        }
        String setter = "set" + capitalize(name);
        try {
            Method[] methods = findMethods(o.getClass());
            Method setPropertyMethodVoid = null;
            Method setPropertyMethodBool = null;
            for (Method method : methods) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (setter.equals(method.getName())
                        && parameterTypes.length == 1
                        && "java.lang.String".equals(parameterTypes[0].getName())) {
                    method.invoke(o, value);
                    return true;
                }
            }
            // try a setFoo(int) or setFoo(boolean)
            for (Method method : methods) {
                boolean ok = true;
                if (setter.equals(method.getName())
                        && method.getParameterTypes().length == 1) {
                    Class<?> paramType = method.getParameterTypes()[0];
                    Object[] params = new Object[1];

                    switch (paramType.getName()) {
                        case "java.lang.Integer":
                        case "int": {
                            try {
                                params[0] = Integer.valueOf(value);
                            } catch (NumberFormatException e) {
                                ok = false;
                            }
                            break;
                        }
                        case "java.lang.Long":
                        case "long": {
                            try {
                                params[0] = Long.valueOf(value);
                            } catch (NumberFormatException e) {
                                ok = false;
                            }
                            break;
                        }
                        case "java.lang.Boolean":
                        case "boolean": {
                            params[0] = Boolean.valueOf(value);
                            break;
                        }
                        default: {
                            if (logger.isDebugEnabled()) {
                                logger.debug("IntrospectionUtils: Unknown type {}", paramType.getName());
                            }
                        }
                    }

                    if (ok) {
                        method.invoke(o, params);
                        return true;
                    }
                }
                if ("setProperty".equals(method.getName())) {
                    if (method.getReturnType() == Boolean.TYPE) {
                        setPropertyMethodBool = method;
                    } else {
                        setPropertyMethodVoid = method;
                    }
                }
            }

            // no setXXX found, try a setProperty("name", "value")
            if (invokeSetProperty && (setPropertyMethodBool != null || setPropertyMethodVoid != null)) {
                Object[] params = new Object[2];
                params[0] = name;
                params[1] = value;
                if (setPropertyMethodBool != null) {
                    try {
                        return (Boolean) setPropertyMethodBool.invoke(o, params);
                    } catch (IllegalArgumentException e) {
                        if (setPropertyMethodVoid != null) {
                            setPropertyMethodVoid.invoke(o, params);
                            return true;
                        } else {
                            throw e;
                        }
                    }
                } else {
                    setPropertyMethodVoid.invoke(o, params);
                    return true;
                }
            }
        } catch (IllegalArgumentException e) {
            logger.warn("IAE {} {} {}", o, name, value, e);
        } catch (IllegalAccessException e) {
            logger.warn("IntrospectionUtils: IllegalAccessException for {} {}={}", o, name, value, e);
        } catch (InvocationTargetException e) {
            logger.warn("IntrospectionUtils: InvocationTargetException for {} {}={}", o, name, value, e);
        }
        return false;
    }

    public static Object getProperty(Object o, String name) {
        String getter = "get" + capitalize(name);
        String isGetter = "is" + capitalize(name);
        try {
            Method[] methods = findMethods(o.getClass());
            Method getPropertyMethod = null;

            for (Method method : methods) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (getter.equals(method.getName()) && parameterTypes.length == 0) {
                    return method.invoke(o, (Object[]) null);
                }
                if (isGetter.equals(method.getName()) && parameterTypes.length == 0) {
                    return method.invoke(o, (Object[]) null);
                }
                if ("getProperty".equals(method.getName())) {
                    getPropertyMethod = method;
                }
            }

            // no getXXX found, try a getProperty("name")
            if (getPropertyMethod != null) {
                Object[] params = new Object[1];
                params[0] = name;
                return getPropertyMethod.invoke(o, params);
            }
        } catch (IllegalArgumentException ex2) {
            logger.warn("IAE {} {}", o, name, ex2);
        } catch (IllegalAccessException e) {
            logger.warn("IntrospectionUtils: IllegalAccessException for {} {}", o, name, e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof NullPointerException) {
                return null;
            }
            logger.warn("IntrospectionUtils: InvocationTargetException for {} {}", o, name, e);
        }
        return null;
    }

    /**
     * Replace ${NAME} with the property value.
     *
     * @param value       the value
     * @param staticProp  replacement properties
     * @param dynamicProp replacement properties
     * @return the replacement value
     */
    public static String replaceProperties(String value, Hashtable<Object, Object> staticProp,
            PropertySource dynamicProp[]) {
        if (value.indexOf('$') < 0) {
            return value;
        }
        StringBuilder sb = new StringBuilder();
        int prev = 0;
        int pos;
        while ((pos = value.indexOf('$', prev)) >= 0) {
            if (pos > 0) {
                sb.append(value.substring(prev, pos));
            }
            if (pos == (value.length() - 1)) {
                sb.append('$');
                prev = pos + 1;
            } else if (value.charAt(pos + 1) != '{') {
                sb.append('$');
                prev = pos + 1;
            } else {
                int endName = value.indexOf('}', pos);
                if (endName < 0) {
                    sb.append(value.substring(pos));
                    prev = value.length();
                    continue;
                }
                String n = value.substring(pos + 2, endName);
                String v = null;
                if (staticProp != null) {
                    v = (String) staticProp.get(n);
                }
                if (v == null && dynamicProp != null) {
                    for (PropertySource source : dynamicProp) {
                        v = source.getProperty(n);
                        if (v != null) {
                            break;
                        }
                    }
                }
                if (v == null) {
                    v = "${" + n + "}";
                }
                sb.append(v);
                prev = endName + 1;
            }
        }
        if (prev < value.length()) {
            sb.append(value.substring(prev));
        }
        return sb.toString();
    }

    /**
     * Reverse of {@link java.beans.Introspector#decapitalize(java.lang.String)}.
     *
     * @param name The string to be capitalized.
     * @return The capitalized version of the string.
     */
    public static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    public static void clear() {
        objectMethods.clear();
    }

    public static Method[] findMethods(Class<?> c) {
        Method[] methods = objectMethods.get(c);
        if (methods != null) {
            return methods;
        }
        methods = c.getMethods();
        objectMethods.put(c, methods);
        return methods;
    }

    public static Method findMethod(Class<?> c, String methodName, Class<?>[] params) {
        Method[] methods = findMethods(c);
        if (methods == null) {
            return null;
        }
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if ((parameterTypes == null || parameterTypes.length == 0) && (params == null || params.length == 0)) {
                    return method;
                }
                if (!(parameterTypes != null && params != null && params.length == parameterTypes.length)) {
                    continue;
                }
                boolean found = true;
                for (int i = 0; i < params.length; i++) {
                    if (params[i] != parameterTypes[i]) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    return method;
                }
            }
        }
        return null;
    }

    public static Object callMethod1(Object target, String methodN, Object param1, String typeParam1, ClassLoader cl)
            throws Exception {
        if (target == null || param1 == null) {
            throw new IllegalArgumentException("IntrospectionUtils: Assert: Illegal params " + target + " " + param1);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("IntrospectionUtils: callMethod {} {} {}",
                    target.getClass().getName(), param1.getClass().getName(), typeParam1);
        }
        Class<?>[] params = new Class[1];
        if (typeParam1 == null) {
            params[0] = param1.getClass();
        } else {
            params[0] = cl.loadClass(typeParam1);
        }
        Method m = findMethod(target.getClass(), methodN, params);
        if (m == null) {
            throw new NoSuchMethodException(target.getClass().getName() + " " + methodN);
        }
        return m.invoke(target, param1);
    }

    public static Object callMethodN(Object target, String methodN, Object[] params, Class<?>[] typeParams)
            throws Exception {
        Method m = findMethod(target.getClass(), methodN, typeParams);
        if (m == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("IntrospectionUtils: Cannot find method {} in {} class {}",
                        methodN, target, target.getClass());
            }
            return null;
        }
        Object result = m.invoke(target, params);
        if (logger.isDebugEnabled()) {
            logger.debug("IntrospectionUtils: {}.{}({})",
                    target.getClass().getName(), methodN, StringUtils.join(params, ","));
        }
        return result;
    }

    public static Object callMethodN(Object target, String methodN, Object[] params, String[] signature)
            throws Exception {
        assert signature != null;
        Class<?>[] typeParams = new Class[signature.length];
        for (int i = 0; i < signature.length; i++) {
            typeParams[i] = Class.forName(signature[i]);
        }
        return callMethodN(target, methodN, params, typeParams);
    }

    public static <T> T convert(String object, Class<T> paramType) {
        Object result = null;
        switch (paramType.getName()) {
            case "java.lang.String": {
                result = object;
                break;
            }
            case "java.lang.Integer":
            case "int": {
                try {
                    result = Integer.valueOf(object);
                } catch (NumberFormatException ignore) {
                }
                break;
            }
            case "java.lang.Boolean":
            case "boolean": {
                result = Boolean.valueOf(object);
                break;
            }
            default: {
                if (logger.isDebugEnabled()) {
                    logger.debug("IntrospectionUtils: Unknown type " + paramType.getName());
                }
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("Cannot convert argument " + object + " to type " + paramType.getName());
        }
        //noinspection unchecked
        return (T) result;
    }

    public static interface PropertySource {
        public String getProperty(String key);
    }
}
