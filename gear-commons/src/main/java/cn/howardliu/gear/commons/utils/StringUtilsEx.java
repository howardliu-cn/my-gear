package cn.howardliu.gear.commons.utils;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * <br>created at 18-1-25
 *
 * @author liuxh
 * @author zhaorunhong
 * @since 0.1.0
 */
public class StringUtilsEx extends StringUtils {
    /**
     * 获取输入字符串的utf-8编码格式的byte数组
     *
     * @return utf-8编码格式的byte数组,如果输入字符串为空,返回空数组
     */
    public static byte[] bytes(String s) {
        if (s == null) {
            return new byte[]{};
        }
        return s.getBytes(StandardCharsets.UTF_8);
    }

    public static String str(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * 获取uuid
     * @return UUID
     */
    public static  String getUuid(){
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }


    /**
     * 获取手机号后四位
     */
    public static String getFourNumMobile(String mobile) {
        if(StringUtils.isNotBlank(mobile) && mobile.length()>4) {
            return mobile.substring(mobile.length()-4,mobile.length());
        }else{
            return null;
        }
    }

    /**
     * 获取中间四位****的密文手机号
     */
    public static String getMarkMobile(String mobile) {
        if(StringUtils.isNotBlank(mobile)) {
            return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }else {
            return null;
        }
    }
}
