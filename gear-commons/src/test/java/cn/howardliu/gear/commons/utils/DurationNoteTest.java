package cn.howardliu.gear.commons.utils;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * <br>created at 17-6-26
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class DurationNoteTest {
    @Test
    public void stop() throws Exception {
        DurationNote note = new DurationNote();
        TimeUnit.MILLISECONDS.sleep(1);
        note.stage("1");
        TimeUnit.MILLISECONDS.sleep(2);
        note.stage("2");
        TimeUnit.MILLISECONDS.sleep(3);
        note.stage("3");
        TimeUnit.SECONDS.sleep(1);
        note.stage("4");
        note.stop();
    }
}