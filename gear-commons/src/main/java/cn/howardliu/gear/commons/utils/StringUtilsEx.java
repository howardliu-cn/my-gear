package cn.howardliu.gear.commons.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * <br>created at 2020/5/5
 *
 * @author liuxh
 * @since 1.0.0
 */
public class StringUtilsEx extends StringUtils {
    /**
     * @see org.apache.commons.lang3.StringUtils#isNumeric
     * @deprecated
     */
    @Deprecated
    public static boolean isDigits(String x) {
        return isNumeric(x);
    }

    public static String format1Decimals(Double value, String suffix) {
        String p = String.valueOf(value);
        int ix = p.indexOf(".") + 1;
        int ex = p.indexOf("E");
        char fraction = p.charAt(ix);
        if (fraction == '0') {
            if (ex != -1) {
                return p.substring(0, ix - 1) + p.substring(ex) + suffix;
            } else {
                return p.substring(0, ix - 1) + suffix;
            }
        } else {
            if (ex != -1) {
                return p.substring(0, ix) + fraction + p.substring(ex) + suffix;
            } else {
                return p.substring(0, ix) + fraction + suffix;
            }
        }
    }

    public static String unCapitalize(String str) {
        return changeFirstCharacterCase(str, false);
    }

    public static String changeFirstCharacterCase(String str, Boolean capitalize) {
        if (str == null || str.length() == 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str.length());
        if (capitalize) {
            sb.append(Character.toUpperCase(str.charAt(0)));
        } else {
            sb.append(Character.toLowerCase(str.charAt(0)));
        }
        sb.append(str.substring(1));
        return sb.toString();
    }
}
