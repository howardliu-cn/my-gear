package cn.howardliu.gear.config.service;

/**
 * <br>created at 16-12-20
 *
 * @author liuxh
 * @since 1.0.1
 */
public interface ConfigService {
    String getValue(String fieldName);

    <T> T getValue(String fieldName, Converter<T> converter);

    <T> T getValue(String fieldName, T defaultValue, Converter<T> converter);

    <T> boolean setValue(String fieldName, T defaultValue);

    public static interface Converter<T> {
        T convert(String value);
    }

    public static class IntegerConverter implements Converter<Integer> {
        @Override
        public Integer convert(String value) {
            return Integer.valueOf(value);
        }
    }

    public static class FloatConverter implements Converter<Float> {
        @Override
        public Float convert(String value) {
            return Float.valueOf(value);
        }
    }

    public static class DoubleConverter implements Converter<Double> {
        @Override
        public Double convert(String value) {
            return Double.valueOf(value);
        }
    }

    public static class LongConverter implements Converter<Long> {
        @Override
        public Long convert(String value) {
            return Long.valueOf(value);
        }
    }

    public static class BooleanConverter implements Converter<Boolean> {
        @Override
        public Boolean convert(String value) {
            return Boolean.valueOf(value);
        }
    }

    public static class StringConverter implements Converter<String> {
        @Override
        public String convert(String value) {
            return value == null ? "" : value;
        }
    }
}
