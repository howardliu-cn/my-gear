package cn.howardliu.gear.commons.utils;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * <br>created at 2020/5/5
 *
 * @author liuxh
 * @since 1.0.0
 */
public class Processor {
    public static String execute(String cmd) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            InputStream in = process.getInputStream();
            return IOUtils.toString(new InputStreamReader(in, Charset.defaultCharset()));
        } catch (Exception e) {
            return "(error executing: " + cmd + ")";
        } catch (Error e) {
            final String message = e.getMessage();
            if (message != null && (message.contains("posix_spawn") || message.contains("UNIXProcess"))) {
                return "(error executing: " + cmd + ")";
            }
            throw e;
        } finally {
            if (process != null) {
                IOUtils.closeQuietly(process.getOutputStream());
                IOUtils.closeQuietly(process.getInputStream());
                IOUtils.closeQuietly(process.getErrorStream());
            }
        }
    }

    public static String execute(String[] cmd) {
        final String cmdArrayStr = Arrays.toString(cmd);
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            InputStream in = process.getInputStream();
            return IOUtils.toString(new InputStreamReader(in, Charset.defaultCharset()));
        } catch (Exception e) {
            return "(error executing: " + cmdArrayStr + ")";
        } catch (Error e) {
            final String message = e.getMessage();
            if (message != null && (message.contains("posix_spawn") || message.contains("UNIXProcess"))) {
                return "(error executing: " + cmdArrayStr + ")";
            }
            throw e;
        } finally {
            if (process != null) {
                IOUtils.closeQuietly(process.getOutputStream());
                IOUtils.closeQuietly(process.getInputStream());
                IOUtils.closeQuietly(process.getErrorStream());
            }
        }
    }
}
