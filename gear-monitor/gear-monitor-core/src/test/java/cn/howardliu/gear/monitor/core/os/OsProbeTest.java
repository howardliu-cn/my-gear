package cn.howardliu.gear.monitor.core.os;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * <br>created at 17-5-4
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class OsProbeTest {
    private OsProbe probe;

    @Before
    public void setUp() throws Exception {
        probe = new OsProbe();
    }

    @Test
    public void getCommittedVirtualMemorySize() throws Exception {
    }

    @Test
    public void getTotalSwapSpaceSize() throws Exception {
    }

    @Test
    public void getFreeSwapSpaceSize() throws Exception {
    }

    @Test
    public void getProcessCpuTime() throws Exception {
    }

    @Test
    public void getFreePhysicalMemorySize() throws Exception {
    }

    @Test
    public void getTotalPhysicalMemorySize() throws Exception {
    }

    @Test
    public void getSystemCpuLoad() throws Exception {
    }

    @Test
    public void getSystemLoadAverage() throws Exception {
    }

    @Test
    public void readProcLoadavg() throws Exception {
    }

    @Test
    public void getSystemCpuPercent() throws Exception {
    }

    @Test
    public void getProcessCpuLoad() throws Exception {
    }

}