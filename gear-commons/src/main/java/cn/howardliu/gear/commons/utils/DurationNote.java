package cn.howardliu.gear.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <br>created at 17-6-26
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public final class DurationNote {
    private static final Logger logger = LoggerFactory.getLogger(DurationNote.class);
    private final AtomicInteger num = new AtomicInteger();
    private final Duration basic = Duration.ofMillis(System.currentTimeMillis());
    private final List<String> commentList = Collections.synchronizedList(new ArrayList<String>());
    private final List<Duration> durationList = Collections.synchronizedList(new ArrayList<Duration>());
    private final String name;

    public DurationNote() {
        this(Thread.currentThread().getName() + "-duration");
    }

    public DurationNote(String name) {
        this.name = name;
        stage("initial");
    }

    public synchronized DurationNote refresh() {
        num.set(0);
        commentList.clear();
        durationList.clear();
        return this;
    }

    public DurationNote stage() {
        return stage("stage-" + num.incrementAndGet());
    }

    public synchronized DurationNote stage(String comment) {
        check();
        commentList.add(comment);
        durationList.add(Duration.ofMillis(System.currentTimeMillis()));
        return this;
    }

    private void check() throws NotEqualException {
        if (commentList.size() != durationList.size()) {
            throw new NotEqualException("the size of comments and durations not equal");
        }
    }

    public synchronized DurationNote stop() {
        check();
        int size = commentList.size();
        for (int i = 0; i < size; i++) {
            String comment = commentList.get(i);
            Duration duration = durationList.get(i);
            long stepMills;
            if (i == 0) {
                stepMills = duration.minus(basic).toMillis();
            } else {
                stepMills = duration.minus(durationList.get(i - 1)).toMillis();
            }
            logger.info("name: [{}], stage: [{}], used: [{}ms]", name, comment, stepMills);
            System.out.println("name: [" + name + "], stage: [" + comment + "], used: [" + stepMills + "ms]");
        }
        return this;
    }

    public static class NotEqualException extends RuntimeException {
        public NotEqualException(String message) {
            super(message);
        }
    }
}
