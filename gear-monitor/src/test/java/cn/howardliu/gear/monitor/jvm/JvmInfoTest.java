package cn.howardliu.gear.monitor.jvm;

import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.concurrent.TimeUnit;

/**
 * <br>created at 17-3-9
 *
 * @author liuxh
 * @since 1.0.0
 */
public class JvmInfoTest {
    @Test
    public void test() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
            System.out.println(mxBean.getUptime());
            System.out.println(mxBean.getStartTime());

            JvmInfo jvmInfo = JvmInfo.instance();
            System.out.println(jvmInfo.getRuntimeInfo().getUpTime());
            System.out.println(jvmInfo.getRuntimeInfo().getStartTime());

            TimeUnit.SECONDS.sleep(1);
        }
    }
}