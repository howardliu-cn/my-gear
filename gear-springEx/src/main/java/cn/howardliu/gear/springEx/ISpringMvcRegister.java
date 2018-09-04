package cn.howardliu.gear.springEx;

import java.io.Closeable;

/**
 * <br>created at 16-7-30
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface ISpringMvcRegister extends Closeable {
    void regist(String suffix) throws Exception;

    void refresh() throws Exception;
}
