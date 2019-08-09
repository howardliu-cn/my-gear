package cn.howardliu.gear.spring.boot.web.exception;

/**
 * <br>created at 18-10-8
 *
 * @author liuxh
 * @since 1.0.0
 */
public class RemoteServiceException extends RemoteException {
    public RemoteServiceException() {
    }

    public RemoteServiceException(String message) {
        super(message);
    }

    public RemoteServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteServiceException(Throwable cause) {
        super(cause);
    }

    public RemoteServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
