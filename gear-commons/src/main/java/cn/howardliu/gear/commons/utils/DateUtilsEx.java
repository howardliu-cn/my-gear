package cn.howardliu.gear.commons.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <br>created at 16-5-5
 *
 * @author liuxh
 * @since 1.0.0
 */
public class DateUtilsEx {
    public static final String fmtString = "yyyy-MM-dd HH:mm:ss";
    public static final DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat hasMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static final DateFormat ymdFmt = new SimpleDateFormat("yyyy-MM-dd");
    public static final String fmtInMinuteString = "yyyy-MM-dd HH:mm";
    public static final DateFormat noneSecondFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String nowString() {
        DateFormat fmt = new SimpleDateFormat(fmtString);
        return fmt.format(new Date());
    }

    public static String strInMinute(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat noneSecondFmt = new SimpleDateFormat(fmtInMinuteString);
        return noneSecondFmt.format(date);
    }

    public static String date2String(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat fmt = new SimpleDateFormat(fmtString);
        return fmt.format(date);
    }

    public static Date string2Date(String str) throws ParseException {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        DateFormat fmt = new SimpleDateFormat(fmtString);
        return fmt.parse(str);
    }

    public static Date string2DateNonException(String str) {
        try {
            if (StringUtils.isBlank(str)) {
                return null;
            }
            DateFormat fmt = new SimpleDateFormat(fmtString);
            return fmt.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static boolean checkDate(String str) {
        try {
            string2Date(str);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
