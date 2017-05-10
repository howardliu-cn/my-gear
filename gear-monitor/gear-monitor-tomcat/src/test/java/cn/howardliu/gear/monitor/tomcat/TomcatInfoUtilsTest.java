package cn.howardliu.gear.monitor.tomcat;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.startup.Tomcat;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * <br>created at 17-5-9
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class TomcatInfoUtilsTest {
    private Tomcat tomcat;
    private int port = 6842;

    @Before
    public void setUp() throws LifecycleException, InterruptedException {
        tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.start();
        new Thread(() -> tomcat.getServer().await()).start();
        for (int i = 0; i < 10; i++) {
            if (tomcat.getServer().getState() == LifecycleState.STARTED) {
                break;
            }
            TimeUnit.SECONDS.sleep(1);
        }
    }

    @After
    public void shutdown() throws LifecycleException {
        tomcat.stop();
    }

    @Test
    public void test() throws Exception {
        Assert.assertTrue(TomcatInfoUtils.getPort() == port);
    }
}
