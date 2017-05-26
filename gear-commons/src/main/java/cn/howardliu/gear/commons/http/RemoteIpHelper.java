package cn.howardliu.gear.commons.http;

import javax.servlet.http.HttpServletRequest;

import static cn.howardliu.gear.commons.http.HttpHeader.*;

/**
 * <br>created at 17-5-26
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class RemoteIpHelper {
    public static final String UNKNOWN = "unknown";

    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader(X_FORWARDED_FOR.key());
        if (isNotFound(ip)) {
            ip = request.getHeader(PROXY_CLIENT_IP.key());
        }
        if(isNotFound(ip)) {
            ip = request.getHeader(WL_PROXY_CLIENT_IP.key());
        }
        if(isNotFound(ip)) {
            ip = request.getHeader(HTTP_CLIENT_IP.key());
        }
        if(isNotFound(ip)) {
            ip = request.getHeader(HTTP_X_FORWARDED_FOR.key());
        }
        if(isNotFound(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private static boolean isNotFound(String ip) {
        return ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip);
    }
}
