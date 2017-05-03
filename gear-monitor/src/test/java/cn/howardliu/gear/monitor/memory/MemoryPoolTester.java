package cn.howardliu.gear.monitor.memory;

import org.junit.Assert;
import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.HashSet;
import java.util.Set;

/**
 * <br>created at 17-5-3
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class MemoryPoolTester {
    @Test
    public void test() throws Exception {
        Set<String> memoryPoolNameSet = new HashSet<>();
        for (MemoryPoolMXBean memoryPoolMXBean : ManagementFactory.getMemoryPoolMXBeans()) {
            System.out.println(memoryPoolMXBean.getName());
            memoryPoolNameSet.add(memoryPoolMXBean.getName());
            memoryPoolNameSet.remove(memoryPoolMXBean.getObjectName().getKeyProperty("name"));
        }
        Assert.assertTrue(memoryPoolNameSet.isEmpty());
    }
}
