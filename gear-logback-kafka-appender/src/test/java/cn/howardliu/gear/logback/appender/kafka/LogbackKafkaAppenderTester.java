package cn.howardliu.gear.logback.appender.kafka;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * <br>created at 16-12-23
 *
 * @author liuxh
 * @since 1.0.1
 */
public class LogbackKafkaAppenderTester {
    private static final Logger logger = LoggerFactory.getLogger(LogbackKafkaAppenderTester.class);

    @Test
    public void test() throws Exception {
        logger.debug("this is a test log, the timestamp is " + System.currentTimeMillis());
        TimeUnit.SECONDS.sleep(1);
    }
}
