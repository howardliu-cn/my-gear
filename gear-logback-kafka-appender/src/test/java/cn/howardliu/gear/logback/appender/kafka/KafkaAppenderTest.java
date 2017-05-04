package cn.howardliu.gear.logback.appender.kafka;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import cn.howardliu.gear.logback.appender.kafka.encode.LayoutKafkaMessageEncoder;
import cn.howardliu.gear.logback.appender.kafka.key.RoundRobinKeyingStrategy;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * <br>created at 16-12-24
 *
 * @author liuxh
 * @since 1.0.1
 */
@Ignore
public class KafkaAppenderTest {

    @Test
    public void testAppend() throws Exception {
        KafkaAppender<ILoggingEvent> appender = new KafkaAppender<>();
        appender.addKafkaProducerConfig("bootstrap.servers=10.6.100.4:9092,10.6.100.5:9092,10.6.100.6:9092");
        appender.setTopic("kafka-appender-topic");
        appender.setKeyingStrategy(new RoundRobinKeyingStrategy<String, ILoggingEvent>());
        LayoutKafkaMessageEncoder<ILoggingEvent> encoder = new LayoutKafkaMessageEncoder<>();
        PatternLayout layout = new PatternLayout();
        layout.setPattern("%-5le %d [%t] %C{30}.%M\\(\\) \\(%F:%L\\) - %m%n");
        encoder.setLayout(layout);
        encoder.setCharset(Constants.UTF8);
        appender.setEncoder(encoder);
        appender.start();

        LoggingEvent event = new LoggingEvent();
        event.setMessage("this is a test, the timestamp is " + System.currentTimeMillis());
        appender.append(event);

        TimeUnit.SECONDS.sleep(1);
    }
}