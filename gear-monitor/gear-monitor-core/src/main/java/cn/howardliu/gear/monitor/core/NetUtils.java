package cn.howardliu.gear.monitor.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <br>created at 17-5-5
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public final class NetUtils {
    private static final Logger logger = LoggerFactory.getLogger(NetUtils.class);
    private static final String UNKNOWN_LOCALHOST = "UNKNOWN_LOCALHOST";

    private NetUtils() {
    }

    /**
     * This method gets the network name of the machine we are running on. Returns "UNKNOWN_LOCALHOST" in the unlikely case where the host name cannot be found.
     *
     * @return String the name of the local host
     */
    public static String getLocalHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignored) {
        }
        return UNKNOWN_LOCALHOST;
    }

    /**
     * @return String the address of the local host, or "UNKNOWN_LOCALHOST" when the host address cannot be found.
     */
    public static String getLocalHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ignored) {
        }
        return UNKNOWN_LOCALHOST;
    }
}
