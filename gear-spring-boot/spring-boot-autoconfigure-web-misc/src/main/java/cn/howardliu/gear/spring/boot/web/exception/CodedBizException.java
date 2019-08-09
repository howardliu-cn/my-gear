package cn.howardliu.gear.spring.boot.web.exception;

/**
 * <br>created at 2019-08-10
 *
 * @author liuxh
 * @since 1.0.0
 */
public class CodedBizException extends RuntimeException {
    private final int httpStatus;
    private final String code;
    private boolean logging = true;

    public CodedBizException(String message, int httpStatus, String code) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public CodedBizException(String message, Throwable cause, int httpStatus, String code) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public CodedBizException(String message, String request, int httpStatus, String code) {
        super(message + '\t' + "请求参数:" + request);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public CodedBizException(String message, String request, String response, int httpStatus, String code) {
        super(message + '\t' + "请求参数:" + request + '\t' + "响应结果:" + response);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public CodedBizException(String message, String request, Throwable cause, int httpStatus, String code) {
        super(message + '\t' + "请求参数:" + request, cause);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public CodedBizException(String message, String request, String response, Throwable cause, int httpStatus, String code) {
        super(message + '\t' + "请求参数:" + request + '\t' + "响应结果:" + response, cause);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public CodedBizException(Throwable cause, int httpStatus, String code) {
        super(cause);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public boolean isLogging() {
        return logging;
    }

    public CodedBizException setLogging(boolean logging) {
        this.logging = logging;
        return this;
    }
}
