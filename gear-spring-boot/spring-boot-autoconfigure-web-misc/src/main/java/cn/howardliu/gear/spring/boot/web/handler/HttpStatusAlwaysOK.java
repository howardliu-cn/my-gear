package cn.howardliu.gear.spring.boot.web.handler;

import java.lang.annotation.*;

/**
 * <br>created at 2019-08-10
 *
 * @author liuxh
 * @since 1.0.0
 */
@Target(value = {
        ElementType.TYPE
})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpStatusAlwaysOK {
}
