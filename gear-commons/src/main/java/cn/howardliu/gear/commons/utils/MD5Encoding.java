package cn.howardliu.gear.commons.utils;

import java.security.MessageDigest;

/**
 * <br>created at 16-6-20
 *
 * @author liuxh
 * @since 1.0.0
 */
public class MD5Encoding {
    private final static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String encoding(String text) {
        String encodingStr = null;
        try {
            byte[] strTemp = text.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            encodingStr = new String(str);
        } catch (Exception ignored) {
        }
        return encodingStr;
    }
}
