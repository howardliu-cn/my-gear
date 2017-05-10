package cn.howardliu.gear.monitor.core.process;

import org.junit.Before;
import org.junit.Test;

/**
 * <br>created at 17-5-5
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class ProcessStatsTest {
    private ProcessStats stats;

    @Before
    public void setUp() throws Exception {
        stats = ProcessStats.stats();
    }

    @Test
    public void test() throws Exception {
        System.out.println(stats.getTimestamp());
    }
}