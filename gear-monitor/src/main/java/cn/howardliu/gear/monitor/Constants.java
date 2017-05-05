package cn.howardliu.gear.monitor;

import org.apache.commons.lang3.SystemUtils;

import java.util.Date;
import java.util.StringTokenizer;

/**
 * <br>created at 17-3-9
 *
 * @author liuxh
 * @since 1.0.2
 */
public final class Constants {
    private Constants() {
        // TODO 可以与 org.apache.commons.lang3.SystemUtils 进行对比优化
    }

    public static final Date START_TIME = new Date();

    public static final String JVM_VENDOR = System.getProperty("java.vm.vendor");
    public static final String JVM_VERSION = System.getProperty("java.vm.version");
    public static final String JVM_NAME = System.getProperty("java.vm.name");
    public static final String JVM_SPEC_NAME = System.getProperty("java.vm.specification.name");
    public static final String JVM_SPEC_VERSION = System.getProperty("java.vm.specification.version");
    public static final String JVM_SPEC_VENDOR = System.getProperty("java.vm.specification.vendor");
    public static final String JAVA_CLASS_PATH = System.getProperty("java.class.path");
    public static final String JAVA_LIBRARY_PATH = System.getProperty("java.library.path");
    public static final String JAVA_SPEC_VERSION = System.getProperty("java.specification.version");
    public static final String JAVA_VERSION = System.getProperty("java.version");
    public static final String JAVA_VENDOR = System.getProperty("java.vendor");

    public static final boolean IS_JAVA_1_7 = SystemUtils.IS_JAVA_1_7;
    public static final boolean IS_JAVA_1_8 = SystemUtils.IS_JAVA_1_8;

    private static final int JVM_MAJOR_VERSION;
    private static final int JVM_MINOR_VERSION;

    public static final boolean JRE_IS_64BIT;

    public static final String OS_NAME = System.getProperty("os.name");
    public static final boolean LINUX = OS_NAME.startsWith("Linux");
    public static final boolean WINDOWS = OS_NAME.startsWith("Windows");
    public static final boolean SUN_OS = OS_NAME.startsWith("SunOS");
    public static final boolean MAC_OS_X = OS_NAME.startsWith("Mac OS X");
    public static final boolean FREE_BSD = OS_NAME.startsWith("FreeBSD");
    public static final String OS_ARCH = System.getProperty("os.arch");
    public static final String OS_VERSION = System.getProperty("os.version");

    static {
        final StringTokenizer st = new StringTokenizer(JAVA_SPEC_VERSION, ".");
        JVM_MAJOR_VERSION = Integer.parseInt(st.nextToken());
        if (st.hasMoreTokens()) {
            JVM_MINOR_VERSION = Integer.parseInt(st.nextToken());
        } else {
            JVM_MINOR_VERSION = 0;
        }
        boolean is64Bit = false;
        String dataModel = null;
        try {
            dataModel = System.getProperty("sun.arch.data.model");
            if (dataModel != null) {
                is64Bit = dataModel.contains("64");
            }
        } catch (SecurityException ignored) {}
        if (dataModel == null) {
            is64Bit = OS_ARCH != null && OS_ARCH.contains("64");
        }
        JRE_IS_64BIT = is64Bit;
    }
}
