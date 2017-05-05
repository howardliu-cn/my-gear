package cn.howardliu.gear.monitor.process;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * <br>created at 17-5-5
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class ProcessProbeTest {
    private ProcessProbe probe;

    @Before
    public void setUp() throws Exception {
        probe = new ProcessProbe();
    }

    @Test
    public void testNum() throws Exception {
        assertEquals(1_000_000L, 1000000L);
    }

    @Test
    public void getOpenFileDescriptorCount() throws Exception {
        long openFileDescriptorCount = probe.getOpenFileDescriptorCount();
        System.out.println(openFileDescriptorCount);
        assertTrue(openFileDescriptorCount > 0);
    }

    @Test
    public void getMaxFileDescriptorCount() throws Exception {
        long maxFileDescriptorCount = probe.getMaxFileDescriptorCount();
        System.out.println(maxFileDescriptorCount);
        assertTrue(maxFileDescriptorCount > 0);
    }

    @Test
    public void getProcessCpuTime() throws Exception {
    }

    @Test
    public void getProcessCpuLoad() throws Exception {
    }

    @Test
    public void getCommittedVirtualMemorySize() throws Exception {
    }

}