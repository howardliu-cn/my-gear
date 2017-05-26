package cn.howardliu.gear.commons.http;

/**
 * <br>created at 17-5-26
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public enum HttpHeader {
    X_FORWARDED_FOR("X-Forwarded-For"),
    PROXY_CLIENT_IP("Proxy-Client-IP"),
    WL_PROXY_CLIENT_IP("WL-Proxy-Client-IP"),
    HTTP_CLIENT_IP("HTTP_CLIENT_IP"),
    HTTP_X_FORWARDED_FOR("HTTP_X_FORWARDED_FOR");

    private String key;

    HttpHeader(String key) {
        this.key = key;
    }

    public String key() {
        return this.key;
    }
}
