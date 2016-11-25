package cn.howardliu.gear.springEx;

/**
 * <br/>created at 16-5-6
 *
 * @author liuxh
 * @since 1.0.0
 */
public class DuplicateServiceNameException extends Exception {
    public DuplicateServiceNameException() {
    }

    public DuplicateServiceNameException(String message) {
        super(message);
    }

    public DuplicateServiceNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateServiceNameException(Throwable cause) {
        super(cause);
    }

    public DuplicateServiceNameException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
