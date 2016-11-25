package cn.howardliu.gear.commons.utils;

import cn.howardliu.gear.commons.annotation.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * <br/>created at 16-4-7
 *
 * @author liuxh
 * @since 1.1.17
 */
public class PropertiesUtils {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

    @SuppressWarnings("unchecked")
    public static <T> T parseObject(Properties properties, Class<T> clazz) {
        try {
            Constructor<T> defaultConstructor = clazz.getDeclaredConstructor();
            Object o = defaultConstructor.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                Class<?> fieldType = field.getType();
                Key fieldKey = field.getAnnotation(Key.class);
                if (fieldKey == null) {
                    continue;
                }
                String key = fieldKey.value();
                if (isBlank(key)) {
                    continue;
                }
                String value = properties.getProperty(key);
                // byte, short, int, char, long, float, double, boolean, String
                if (fieldType == String.class) {
                    setStringValue(o, clazz, fieldName, value);
                } else if (fieldType == Byte.class || fieldType == byte.class) {
                    setByteValue(o, clazz, fieldName, value);
                } else if (fieldType == Short.class || fieldType == short.class) {
                    setShortValue(o, clazz, fieldName, value);
                } else if (fieldType == Integer.class || fieldType == int.class) {
                    setIntValue(o, clazz, fieldName, value);
                } else if (fieldType == Character.class || fieldType == char.class) {
                    setCharValue(o, clazz, fieldName, value);
                } else if (fieldType == Long.class || fieldType == long.class) {
                    setLongValue(o, clazz, fieldName, value);
                } else if (fieldType == Float.class || fieldType == float.class) {
                    setFloatValue(o, clazz, fieldName, value);
                } else if (fieldType == Double.class || fieldType == double.class) {
                    setDoubleValue(o, clazz, fieldName, value);
                } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                    setBooleanValue(o, clazz, fieldName, value);
                }
            }
            return (T) o;
//        } catch (NoSuchMethodException e) {
//        } catch (InvocationTargetException e) {
//        } catch (IntrospectionException e) {
//        } catch (InstantiationException e) {
//        } catch (IllegalAccessException e) {
        } catch (Exception e) {
            logger.error("解析数据出错, properties={}, class={}", properties, clazz, e);
        }
        return null;
    }

    private static <T> void setBooleanValue(Object o, Class<T> clazz, String fieldName, String value)
            throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Boolean _value;
        if (isBlank(value)) {
            _value = false;
            logger.warn("{}对应数据为空，将使用默认值false", fieldName);
        } else {
            _value = Boolean.valueOf(value);
        }
        setValue(o, clazz, fieldName, _value);
    }

    private static <T> void setDoubleValue(Object o, Class<T> clazz, String fieldName, String value)
            throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Double _value;
        if (isBlank(value)) {
            _value = 0d;
            logger.warn("{}对应数据为空，将使用默认值0", fieldName);
        } else if (StringUtilsEx.isDoubleNumber(value)) {
            _value = Double.valueOf(value);
        } else {
            throw new IllegalArgumentException("字段" + fieldName + "对应数据不是数字");
        }
        setValue(o, clazz, fieldName, _value);
    }

    private static <T> void setFloatValue(Object o, Class<T> clazz, String fieldName, String value)
            throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Float _value;
        if (isBlank(value)) {
            _value = 0f;
            logger.warn("{}对应数据为空，将使用默认值0", fieldName);
        } else if (StringUtilsEx.isDoubleNumber(value)) {
            _value = Float.valueOf(value);
        } else {
            throw new IllegalArgumentException("字段" + fieldName + "对应数据不是数字");
        }
        setValue(o, clazz, fieldName, _value);
    }

    private static <T> void setLongValue(Object o, Class<T> clazz, String fieldName, String value)
            throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Long _value;
        if (isBlank(value)) {
            _value = 0L;
            logger.warn("{}对应数据为空，将使用默认值0", fieldName);
        } else if (StringUtilsEx.isDigits(value)) {
            _value = Long.valueOf(value);
        } else {
            throw new IllegalArgumentException("字段" + fieldName + "对应数据不是数字");
        }
        setValue(o, clazz, fieldName, _value);
    }

    private static <T> void setCharValue(Object o, Class<T> clazz, String fieldName, String value)
            throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Character _value;
        if (value == null || value.isEmpty()) {
            _value = '\0';
            logger.warn("{}对应数据为null，将使用默认值'\\0'", fieldName);
        } else {
            _value = value.trim().charAt(0);
        }
        setValue(o, clazz, fieldName, _value);
    }

    private static <T> void setShortValue(Object o, Class<T> clazz, String fieldName, String value)
            throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Short _value;
        if (isBlank(value)) {
            _value = 0;
            logger.warn("{}对应数据为空，将使用默认值0", fieldName);
        } else if (StringUtilsEx.isDigits(value)) {
            _value = Short.valueOf(value);
        } else {
            throw new IllegalArgumentException("字段" + fieldName + "对应数据不是数字");
        }
        setValue(o, clazz, fieldName, _value);
    }

    private static <T> void setByteValue(Object o, Class<T> clazz, String fieldName, String value)
            throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Byte _value;
        if (isBlank(value)) {
            _value = 0;
            logger.warn("{}对应数据为空，将使用默认值0", fieldName);
        } else if (StringUtilsEx.isDigits(value)) {
            _value = Byte.valueOf(value);
        } else {
            throw new IllegalArgumentException("字段" + fieldName + "对应数据不是数字");
        }
        setValue(o, clazz, fieldName, _value);
    }

    private static void setIntValue(Object o, Class<?> clazz, String fieldName, String value)
            throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Integer _value;
        if (isBlank(value)) {
            _value = 0;
            logger.warn("{}对应数据为空，将使用默认值0", fieldName);
        } else if (StringUtilsEx.isDigits(value)) {
            _value = Integer.valueOf(value);
        } else {
            throw new IllegalArgumentException("字段" + fieldName + "对应数据不是数字");
        }
        setValue(o, clazz, fieldName, _value);
    }

    private static void setStringValue(Object o, Class<?> clazz, String fieldName, String value)
            throws IllegalAccessException, InvocationTargetException, IntrospectionException {
        setValue(o, clazz, fieldName, value);
    }

    private static void setValue(Object o, Class<?> clazz, String fieldName, Object value)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        PropertyDescriptor propDesc = new PropertyDescriptor(fieldName, clazz);
        Method setMethod = propDesc.getWriteMethod();
        setMethod.invoke(o, value);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
