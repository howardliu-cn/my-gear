package cn.howardliu.gear.es.exception;

/**
 * <br/>created at 16-5-4
 *
 * @author liuxh
 * @since 1.0.0
 */
public class IndexException extends Exception {
    public IndexException() {
    }

    public IndexException(String message) {
        super(message);
    }

    public IndexException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndexException(Throwable cause) {
        super(cause);
    }

    public IndexException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
