package cn.howardliu.gear.monitor.core.jvm;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <br>created at 17-8-21
 *
 * @author liuxh
 * @since 0.0.1
 */
public class JvmStatsTest {
    @Test
    @Ignore
    public void stats() throws Exception {
        Assert.assertNotNull(JvmStats.stats());
    }

}