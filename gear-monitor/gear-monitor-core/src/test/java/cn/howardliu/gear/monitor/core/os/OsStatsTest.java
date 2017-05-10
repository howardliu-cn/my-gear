package cn.howardliu.gear.monitor.core.os;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * <br>created at 17-5-5
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class OsStatsTest {
    @Test
    public void stats() throws Exception {
        assertNotNull(OsStats.stats());
    }
}