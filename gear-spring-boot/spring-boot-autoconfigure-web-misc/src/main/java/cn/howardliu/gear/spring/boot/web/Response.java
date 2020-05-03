package cn.howardliu.gear.spring.boot.web;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.now;

/**
 * <br>created at 2019-08-10
 *
 * @author liuxh
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss.SSS");

    private int code;// 状态码，类似HTTP状态码
    private String error;// 错误说明。通常是异常信息
    private String desc;// 结果描述
    private String path;// 请求路径
    private String timestamp = TIMESTAMP_FORMATTER.format(now());// 报错时间。非请求时间，是错误捕捉时间
    private String bizCode;// 业务状态码
    private String bizDesc;// 业务状态说明

    public Response(int code, HttpStatus status, String desc, String path) {
        this(code, status.getReasonPhrase() + ":" + desc, desc, path);
    }

    public Response(int code, String error, String desc, String path) {
        this(code, error, desc, path, null, null);
    }

    public Response(int code, String error, String desc, String path, String bizCode, String bizDesc) {
        this.code = code;
        this.desc = desc;
        this.error = error;
        this.path = path;
        this.timestamp = TIMESTAMP_FORMATTER.format(LocalDateTime.now());
        this.bizCode = bizCode;
        this.bizDesc = bizDesc;
    }

    private T data;
}
