package cn.howardliu.gear.logback.appender.kafka.encode;

import ch.qos.logback.core.Layout;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;

import static cn.howardliu.gear.logback.appender.kafka.Constants.UTF8;

/**
 * <br>created at 16-12-21
 *
 * @author liuxh
 * @since 1.0.1
 */
public class LayoutKafkaMessageEncoder<E> extends KafkaMessageEncoderBase<E> implements Closeable {
    private Layout<E> layout;
    private Charset charset;

    public LayoutKafkaMessageEncoder() {
    }

    public LayoutKafkaMessageEncoder(Layout<E> layout, Charset charset) {
        this.setLayout(layout);
        this.setCharset(charset);
    }

    @Override
    public void start() {
        if (charset == null) {
            addInfo("No charset specified for LayoutKafkaMessageEncoder. Using default UTF8 encoding.");
            charset = UTF8;
        }
        super.start();
    }

    @Override
    public String encode(E event) {
        return layout.doLayout(event);
    }

    public Layout<E> getLayout() {
        return layout;
    }

    public void setLayout(Layout<E> layout) {
        layout.start();
        this.layout = layout;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    @Override
    public void close() throws IOException {
        stop();
        if(this.layout != null) {
            this.layout.stop();
        }
    }
}
