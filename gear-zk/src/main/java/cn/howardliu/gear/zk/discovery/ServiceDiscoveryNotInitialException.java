package cn.howardliu.gear.zk.discovery;

/**
 * <br>created at 16-5-6
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ServiceDiscoveryNotInitialException extends Exception {
    public ServiceDiscoveryNotInitialException() {
    }

    public ServiceDiscoveryNotInitialException(String message) {
        super(message);
    }

    public ServiceDiscoveryNotInitialException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceDiscoveryNotInitialException(Throwable cause) {
        super(cause);
    }

    public ServiceDiscoveryNotInitialException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
