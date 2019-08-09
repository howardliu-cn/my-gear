package cn.howardliu.gear.spring.boot.web.exception;

/**
 * <br>created at 18-11-12
 *
 * @author liuxh
 * @since 1.0.0
 */
public class RemoteException extends Exception {
    public RemoteException() {
    }

    public RemoteException(String message) {
        super(message);
    }

    public RemoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteException(Throwable cause) {
        super(cause);
    }

    public RemoteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
