package cn.howardliu.gear.monitor.jvm;

import org.junit.Assert;
import org.junit.Test;

/**
 * <br>created at 17-5-4
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class PIDTest {
    @Test
    public void getPID() throws Exception {
        Assert.assertTrue(PID.getPID() > 0);
    }

}