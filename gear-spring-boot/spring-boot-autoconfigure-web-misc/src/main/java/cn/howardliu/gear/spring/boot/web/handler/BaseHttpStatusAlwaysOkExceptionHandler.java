package cn.howardliu.gear.spring.boot.web.handler;

import cn.howardliu.gear.spring.boot.web.exception.CodedBizException;
import cn.howardliu.gear.spring.boot.web.Response;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;

/**
 * <br>created at 2019-08-10
 *
 * @author liuxh
 * @since 1.0.0
 */
@RestControllerAdvice(annotations = {
        HttpStatusAlwaysOK.class
})
public class BaseHttpStatusAlwaysOkExceptionHandler {
    private final BaseExceptionHandler commonHandling;

    public BaseHttpStatusAlwaysOkExceptionHandler(BaseExceptionHandler commonHandling) {
        this.commonHandling = commonHandling;
    }

    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestPartException.class,
            BindException.class,
            NoHandlerFoundException.class,
            AsyncRequestTimeoutException.class
    })
    public final ResponseEntity<Object> handleException(Exception ex, WebRequest request) throws Exception {
        final ResponseEntity<Object> responseEntity = this.commonHandling.handleException(ex, request);
        final Object body = responseEntity.getBody();
        if (body != null) {
            return ResponseEntity.ok(body);
        }
        return responseEntity;
    }

    @ExceptionHandler(value = CodedBizException.class)
    public ResponseEntity<Response> handle(final CodedBizException ex, WebRequest request) {
        final ResponseEntity<Response> responseEntity = this.commonHandling.handle(ex, request);
        final Response body = responseEntity.getBody();
        if (body != null) {
            return ResponseEntity.ok(body);
        }
        return responseEntity;
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Response> handle(final ConstraintViolationException ex, WebRequest request) {
        final ResponseEntity<Response> responseEntity = this.commonHandling.handle(ex, request);
        final Response body = responseEntity.getBody();
        if (body != null) {
            return ResponseEntity.ok(body);
        }
        return responseEntity;
    }

    @ExceptionHandler(HystrixRuntimeException.class)
    public ResponseEntity<Response> handle(@NonNull final HystrixRuntimeException hystrixEx, WebRequest request) {
        final ResponseEntity<Response> responseEntity = this.commonHandling.handle(hystrixEx, request);
        final Response body = responseEntity.getBody();
        if (body != null) {
            return ResponseEntity.ok(body);
        }
        return responseEntity;
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Response> handle(final Throwable throwable, WebRequest request) {
        final ResponseEntity<Response> responseEntity = this.commonHandling.handle(throwable, request);
        final Response body = responseEntity.getBody();
        if (body != null) {
            return ResponseEntity.ok(body);
        }
        return responseEntity;
    }
}
