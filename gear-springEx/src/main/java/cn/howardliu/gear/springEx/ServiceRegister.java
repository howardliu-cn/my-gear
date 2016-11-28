package cn.howardliu.gear.springEx;

import java.lang.annotation.*;

/**
 * <br>created at 16-5-5
 *
 * @author liuxh
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceRegister {
    String value();

    String prefix() default "";

    String suffix() default ".json";
}
