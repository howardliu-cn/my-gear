package cn.howardliu.gear.monitor.core.unit;

import cn.howardliu.gear.commons.utils.StringUtilsEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <br>created at 17-2-21
 *
 * @author liuxh
 * @since 1.0.2
 */
public class TimeValue implements Comparable<TimeValue> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final long NANO_SEC_PER_MILLI_SEC = TimeUnit.NANOSECONDS.convert(1, TimeUnit.MILLISECONDS);

    private static Map<TimeUnit, Byte> TIME_UNIT_BYTE_MAP;
    private static Map<Byte, TimeUnit> BYTE_TIME_UNIT_MAP;

    public static final TimeValue MINUS_ONE = timeValueMillis(-1);
    public static final TimeValue ZERO = timeValueMillis(0);

    private final long duration;
    private final TimeUnit timeUnit;

    private static final long C0 = 1L;
    private static final long C1 = C0 * 1000L;
    private static final long C2 = C1 * 1000L;
    private static final long C3 = C2 * 1000L;
    private static final long C4 = C3 * 60L;
    private static final long C5 = C4 * 60L;
    private static final long C6 = C5 * 24L;

    static {
        final Map<TimeUnit, Byte> timeUnitByteMap = new HashMap<>();
        timeUnitByteMap.put(TimeUnit.NANOSECONDS, (byte)0);
        timeUnitByteMap.put(TimeUnit.MICROSECONDS, (byte)1);
        timeUnitByteMap.put(TimeUnit.MILLISECONDS, (byte)2);
        timeUnitByteMap.put(TimeUnit.SECONDS, (byte)3);
        timeUnitByteMap.put(TimeUnit.MINUTES, (byte)4);
        timeUnitByteMap.put(TimeUnit.HOURS, (byte)5);
        timeUnitByteMap.put(TimeUnit.DAYS, (byte)6);

        final Set<Byte> bytes = new HashSet<>();
        for (TimeUnit value : TimeUnit.values()) {
            assert timeUnitByteMap.containsKey(value) : value;
            assert bytes.add(timeUnitByteMap.get(value));
        }

        final Map<Byte, TimeUnit> byteTimeUnitMap = new HashMap<>();
        for (Map.Entry<TimeUnit, Byte> entry : timeUnitByteMap.entrySet()) {
            byteTimeUnitMap.put(entry.getValue(), entry.getKey());
        }

        TIME_UNIT_BYTE_MAP = Collections.unmodifiableMap(timeUnitByteMap);
        BYTE_TIME_UNIT_MAP = Collections.unmodifiableMap(byteTimeUnitMap);
    }

    public TimeValue(long millis) {
        this(millis, TimeUnit.MILLISECONDS);
    }

    public TimeValue(long duration, TimeUnit timeUnit) {
        this.duration = duration;
        this.timeUnit = timeUnit;
    }

    public static TimeValue timeValueNanos(long nanos) {
        return new TimeValue(nanos, TimeUnit.NANOSECONDS);
    }

    public static TimeValue timeValueMillis(long millis) {
        return new TimeValue(millis, TimeUnit.MILLISECONDS);
    }

    public static TimeValue timeValueSeconds(long seconds) {
        return new TimeValue(seconds, TimeUnit.SECONDS);
    }

    public static TimeValue timeValueMinutes(long minutes) {
        return new TimeValue(minutes, TimeUnit.MINUTES);
    }

    public static TimeValue timeValueHours(long hours) {
        return new TimeValue(hours, TimeUnit.HOURS);
    }

    public long nanos() {
        return timeUnit.toNanos(duration);
    }

    public long getNanos() {
        return nanos();
    }

    public long micros() {
        return timeUnit.toMicros(duration);
    }

    public long getMicros() {
        return micros();
    }

    public long millis() {
        return timeUnit.toMillis(duration);
    }

    public long getMillis() {
        return millis();
    }

    public long seconds() {
        return timeUnit.toSeconds(duration);
    }

    public long getSeconds() {
        return seconds();
    }

    public long minutes() {
        return timeUnit.toMinutes(duration);
    }

    public long getMinutes() {
        return minutes();
    }

    public long hours() {
        return timeUnit.toHours(duration);
    }

    public long getHours() {
        return hours();
    }

    public long days() {
        return timeUnit.toDays(duration);
    }

    public long getDays() {
        return days();
    }

    public double microsFrac() {
        return ((double) nanos()) / C1;
    }

    public double getMicrosFrac() {
        return microsFrac();
    }

    public double millisFrac() {
        return ((double) nanos()) / C2;
    }

    public double getMillisFrac() {
        return millisFrac();
    }

    public double secondsFrac() {
        return ((double) nanos()) / C3;
    }

    public double getSecondsFrac() {
        return secondsFrac();
    }

    public double minutesFrac() {
        return ((double) nanos()) / C4;
    }

    public double getMinutesFrac() {
        return minutesFrac();
    }

    public double hoursFrac() {
        return ((double) nanos()) / C5;
    }

    public double getHoursFrac() {
        return hoursFrac();
    }

    public double daysFrac() {
        return ((double) nanos()) / C6;
    }

    public double getDaysFrac() {
        return daysFrac();
    }

    public long getDuration() {
        return this.duration;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && this.compareTo(((TimeValue) o)) == 0;
    }

    @Override
    public int hashCode() {
        return Double.valueOf(((double) duration) * timeUnit.toNanos(1)).hashCode();
    }

    public static long nsecToMSec(long ns) {
        return ns / NANO_SEC_PER_MILLI_SEC;
    }

    @Override
    public int compareTo(TimeValue timeValue) {
        double thisValue = ((double) duration) * timeUnit.toNanos(1);
        double otherValue = ((double) timeValue.duration) * timeValue.timeUnit.toNanos(1);
        return Double.compare(thisValue, otherValue);
    }

    @Override
    public String toString() {
        if (duration < 0) {
            return Long.toString(duration);
        }
        long nanos = nanos();
        if (nanos == 0) {
            return "0s";
        }
        double value = nanos;
        String suffix = "nanos";
        if (nanos >= C6) {
            value = daysFrac();
            suffix = "d";
        } else if (nanos >= C5) {
            value = hoursFrac();
            suffix = "h";
        } else if (nanos >= C4) {
            value = minutesFrac();
            suffix = "m";
        } else if (nanos >= C3) {
            value = secondsFrac();
            suffix = "s";
        } else if (nanos >= C2) {
            value = millisFrac();
            suffix = "ms";
        } else if (nanos >= C1) {
            value = microsFrac();
            suffix = "micros";
        }
        return StringUtilsEx.format1Decimals(value, suffix);
    }
}
