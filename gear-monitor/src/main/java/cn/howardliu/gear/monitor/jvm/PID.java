package cn.howardliu.gear.monitor.jvm;

import cn.howardliu.gear.commons.utils.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.management.VMManagement;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <br>created at 16-12-9
 *
 * @author liuxh
 * @since 1.0.2
 */
public class PID {
    private static final Logger logger = LoggerFactory.getLogger(PID.class);
    private static Integer pid = getPID();

    public static Integer getPID() {
        if (pid == null) {
            pid = getPidByName();
        }
        if (pid == null) {
            pid = getPidByReflect();
        }
        if (pid == null) {
            pid = getPidByCmd();
        }
        if (pid == null) {
            pid = 0;
        }
        return pid;
    }

    public static Integer getPidByName() {
        String xPid = ManagementFactory.getRuntimeMXBean().getName();
        try {
            xPid = xPid.split("@")[0];
            return Integer.parseInt(xPid);
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer getPidByReflect() {
        try {
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            Field jvm = runtime.getClass().getDeclaredField("jvm");
            jvm.setAccessible(true);
            VMManagement vmMgmt = (VMManagement) jvm.get(runtime);
            Method pid_method = vmMgmt.getClass().getDeclaredMethod("getProcessId");
            pid_method.setAccessible(true);
            return (Integer) pid_method.invoke(vmMgmt);
        } catch (InvocationTargetException e) {
            logger.error("VMManagement中没有getProcessId方法，请检查当前JVM版本", e);
        } catch (NoSuchMethodException e) {
            logger.error("获取VMManagement中的getProcessId方法失败，请检查当前的JVM版本", e);
        } catch (IllegalAccessException e) {
            logger.error("通过反射获取MXBean中的VMManagement对象失败", e);
        } catch (NoSuchFieldException e) {
            logger.error("MXBean中没有JVM参数，请检查当前JVM版本", e);
        }
        return 0;
    }

    public static Integer getPidByCmd() {
        try {
            String[] cmd;
            String osName = System.getProperty("os.name");
            if (osName.toLowerCase().contains("window")) {
                // TODO window中获取进程号
                cmd = new String[]{};
            } else {
                cmd = new String[]{"/bin/sh", "-c", "echo $PPID"};
            }
            return Integer.valueOf(Processor.execute(cmd));
        } catch (Exception e) {
            logger.error("通过命令查询进程号失败", e);
        }
        return null;
    }
}
