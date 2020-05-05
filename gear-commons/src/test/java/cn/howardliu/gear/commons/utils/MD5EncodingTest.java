package cn.howardliu.gear.commons.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * <br>created at 2020/5/5
 *
 * @author liuxh
 * @since 1.0.0
 */
public class MD5EncodingTest {
    @Test
    public void encoding() {
        Assert.assertEquals("21232f297a57a5a743894a0e4a801fc3", MD5Encoding.encoding("admin"));
    }
}
