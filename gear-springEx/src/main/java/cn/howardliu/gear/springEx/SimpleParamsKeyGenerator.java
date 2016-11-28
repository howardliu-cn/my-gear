package cn.howardliu.gear.springEx;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * <br>created at 16-5-11
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SimpleParamsKeyGenerator implements KeyGenerator {
    private static final Logger logger = LoggerFactory.getLogger(SimpleParamsKeyGenerator.class);

    @Override
    public Object generate(Object target, Method method, Object... params) {
        if (params == null) {
            return "[]";
        }
        List<String> list = new ArrayList<>(params.length);
        for (Object param : params) {
            if (param != null) {
                list.add(param.toString());
            }
        }
        return "[" + StringUtils.join(list, "-") + "]";
    }
}
