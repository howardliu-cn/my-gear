package cn.howardliu.gear.spring.boot.web.handler;

import cn.howardliu.gear.spring.boot.web.Response;
import cn.howardliu.gear.spring.boot.web.exception.CodedBizException;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
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
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * <br>created at 2019-08-10
 *
 * @author liuxh
 * @since 1.0.0
 */
@RestControllerAdvice
public class BaseExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ResponseEntityExceptionHandler delegate = new ResponseEntityExceptionHandler() {
        @Override
        @NonNull
        protected ResponseEntity<Object> handleExceptionInternal(
                @NonNull Exception ex, Object body, HttpHeaders headers, HttpStatus status, @NonNull WebRequest request) {
            if (ex instanceof MethodArgumentNotValidException) {
                return handle((MethodArgumentNotValidException) ex, request);
            }
            final int statusValue = status.value();
            if (500 <= statusValue && statusValue < 600) {
                logger.error("internal error caught:", ex);
            }
            if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
                request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
            }
            final String requestURI = extractRequestURI(request);
            final Response<?> error = new Response<>(statusValue, status, ex.getMessage(), requestURI);
            return new ResponseEntity<>(error, headers, status);
        }
    };

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
        return this.delegate.handleException(ex, request);
    }

    @ExceptionHandler(value = CodedBizException.class)
    public ResponseEntity<Response<?>> handle(final CodedBizException ex, WebRequest request) {
        return codedBizExceptionHandle(ex, request);
    }

    private ResponseEntity<Response<?>> codedBizExceptionHandle(final CodedBizException ex, WebRequest request) {
        final Throwable cause = ex.getCause();
        final String message = ex.getMessage();
        if (ex.isLogging()) {
            if (cause != null) {
                logger.error("{}", message, cause);
            } else {
                logger.error("{}", message, ex);
            }
        }
        final String requestURI = extractRequestURI(request);
        final Response<?> error;
        final int code = ex.getHttpStatus();
        final HttpStatus resolve = HttpStatus.resolve(code);
        final String bizCode = ex.getCode();
        if (cause != null) {
            final String causeMessage = cause.getMessage();
            if (resolve != null) {
                error = new Response<>(code, resolve.getReasonPhrase(), causeMessage, requestURI, bizCode, message);
            } else {
                error = new Response<>(code, "", causeMessage, requestURI, bizCode, message);
            }
        } else {
            if (resolve != null) {
                error = new Response<>(
                        code, resolve.getReasonPhrase(), message, requestURI, bizCode, message);
            } else {
                error = new Response<>(
                        code, "", message, requestURI, bizCode, message);
            }
        }
        return ResponseEntity.status(code).body(error);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Response<?>> handle(final ConstraintViolationException ex, WebRequest request) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        final String requestURI = extractRequestURI(request);
        final Response<?> descriptor = new Response<>(status.value(), status,
                ex.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage).reduce((s, s2) -> s + "," + s2).orElse(""),
                requestURI);
        return ResponseEntity.status(status).body(descriptor);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Response<?>> handle(final Throwable throwable, WebRequest request) {
        logger.error("unexpected exception caught:", throwable);
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        final String requestURI = extractRequestURI(request);
        final Response<?> error = new Response<>(status.value(), status, throwable.getMessage(), requestURI);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(HystrixRuntimeException.class)
    public ResponseEntity<Response<?>> handle(@NonNull final HystrixRuntimeException hystrixEx, WebRequest request) {
        final CodedBizException de = tryExtractCodedBizException(hystrixEx);
        if (de != null) {
            return this.handle(de, request);
        }
        return handle((Throwable) hystrixEx, request);
    }

    @Nullable
    private CodedBizException tryExtractCodedBizException(@NonNull final HystrixRuntimeException hystrixEx) {
        Throwable ex = hystrixEx;
        Throwable cause = ex.getCause();
        while (cause != null && cause != ex && !(cause instanceof CodedBizException)) {
            ex = cause;
            cause = cause.getCause();
        }
        if (cause instanceof CodedBizException) {
            return (CodedBizException) cause;
        }
        ex = hystrixEx.getFallbackException();
        cause = ex.getCause();
        while (cause != null && cause != ex && !(cause instanceof CodedBizException)) {
            ex = cause;
            cause = cause.getCause();
        }
        if (cause instanceof CodedBizException) {
            return (CodedBizException) cause;
        }
        return null;
    }

    private ResponseEntity<Object> handle(final MethodArgumentNotValidException ex, WebRequest request) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        final String requestURI = extractRequestURI(request);
        final Response<?> descriptor = new Response<>(status.value(), status,
                ex.getBindingResult().getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .reduce((s, s2) -> s + "," + s2).orElse(""),
                requestURI);
        return ResponseEntity.status(status).body(descriptor);
    }

    private String extractRequestURI(WebRequest request) {
        final String requestURI;
        if (request instanceof ServletWebRequest) {
            ServletWebRequest servletWebRequest = (ServletWebRequest) request;
            requestURI = servletWebRequest.getRequest().getRequestURI();
        } else {
            requestURI = "[can't detect]";
        }
        return requestURI;
    }
}
