package cn.howardliu.gear.commons.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <br/>created at 16-5-3
 *
 * @author liuxh
 * @since 1.0.0
 */
public class UsingMemoryCache<T> implements Cache<T> {
    private static final Logger logger = LoggerFactory.getLogger(UsingMemoryCache.class);
    private final Map<String, T> values = new ConcurrentHashMap<>();

    @Override
    public Collection<String> keys() {
        return values.keySet();
    }

    @Override
    public T get(String k) {
        return values.get(k);
    }

    @Override
    public void put(String k, T v) {
        values.put(k, v);
    }

    @Override
    public T remove(String k) {
        if (values.containsKey(k)) {
            return values.remove(k);
        }
        return null;
    }
}
