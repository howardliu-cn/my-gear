package cn.howardliu.gear.spring.boot.web.exception;

/**
 * <br>created at 18-10-9
 *
 * @author liuxh
 * @since 1.0.0
 */
public class RemoteErrorResultException extends RemoteException {
    public RemoteErrorResultException() {
    }

    public RemoteErrorResultException(String message) {
        super(message);
    }

    public RemoteErrorResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteErrorResultException(Throwable cause) {
        super(cause);
    }

    public RemoteErrorResultException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
