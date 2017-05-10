package cn.howardliu.gear.monitor.core.memory;

import org.junit.Assert;
import org.junit.Test;

import javax.management.ObjectName;
import java.lang.management.BufferPoolMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <br>created at 17-5-3
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class BufferPoolTester {
    @Test
    public void test() throws Exception {
        Set<String> objectNameSet = new HashSet<>();
        Set<ObjectName> nioBufferPools = ManagementFactory.getPlatformMBeanServer()
                .queryNames(new ObjectName("java.nio:type=BufferPool,*"), null);
        for (ObjectName objectName : nioBufferPools) {
            objectNameSet.add(objectName.toString());
        }

        List<BufferPoolMXBean> bufferPools = ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class);
        for (BufferPoolMXBean bufferPool : bufferPools) {
            objectNameSet.remove(bufferPool.getObjectName().toString());
        }

        Assert.assertTrue(objectNameSet.isEmpty());
    }
}
