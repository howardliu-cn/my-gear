package cn.howardliu.gear.monitor.unit;

import cn.howardliu.gear.commons.utils.StringUtilsEx;

import java.util.Locale;
import java.util.Objects;

/**
 * <br>created at 16-12-27
 *
 * @author liuxh
 * @since 1.0.2
 */
public class ByteSizeValue implements Comparable<ByteSizeValue> {
    private final long size;
    private final ByteSizeUnit unit;

    public ByteSizeValue(long size) {
        this.size = size;
        this.unit = ByteSizeUnit.BYTES;
    }

    public ByteSizeValue(long size, ByteSizeUnit unit) {
        this.size = size;
        this.unit = unit;
    }

    public int bytesAsInt() {
        long bytes = getBytes();
        if (bytes > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("size [" + toString() + "] is bigger than max int");
        }
        return (int) bytes;
    }

    public long getBytes() {
        return unit.toBytes(size);
    }

    public long getKb() {
        return unit.toKB(size);
    }

    public long getMb() {
        return unit.toMB(size);
    }

    public long getGb() {
        return unit.toGB(size);
    }

    public long getTb() {
        return unit.toTB(size);
    }

    public long getPb() {
        return unit.toPB(size);
    }

    public double getKbFraction() {
        return ((double) getBytes()) / ByteSizeUnit.C1;
    }

    public double getMbFraction() {
        return ((double) getBytes()) / ByteSizeUnit.C2;
    }

    public double getGbFraction() {
        return ((double) getBytes()) / ByteSizeUnit.C3;
    }

    public double getTbFraction() {
        return ((double) getBytes()) / ByteSizeUnit.C4;
    }

    public double getPbFraction() {
        return ((double) getBytes()) / ByteSizeUnit.C5;
    }

    public static ByteSizeValue parseBytesSizeValue(String sValue, String settingName) throws ParseException {
        return parseBytesSizeValue(sValue, null, settingName);
    }

    public static ByteSizeValue parseBytesSizeValue(String sValue, ByteSizeValue defaultValue, String settingName)
            throws ParseException {
        settingName = Objects.requireNonNull(settingName);
        if (sValue == null) {
            return defaultValue;
        }
        long bytes;
        try {
            String lowerSValue = sValue.toLowerCase(Locale.ROOT).trim();
            if (lowerSValue.endsWith("k")) {
                bytes = (long) (Double
                        .parseDouble(lowerSValue.substring(0, lowerSValue.length() - 1)) * ByteSizeUnit.C1);
            } else if (lowerSValue.endsWith("kb")) {
                bytes = (long) (Double
                        .parseDouble(lowerSValue.substring(0, lowerSValue.length() - 2)) * ByteSizeUnit.C1);
            } else if (lowerSValue.endsWith("m")) {
                bytes = (long) (Double
                        .parseDouble(lowerSValue.substring(0, lowerSValue.length() - 1)) * ByteSizeUnit.C2);
            } else if (lowerSValue.endsWith("mb")) {
                bytes = (long) (Double
                        .parseDouble(lowerSValue.substring(0, lowerSValue.length() - 2)) * ByteSizeUnit.C2);
            } else if (lowerSValue.endsWith("g")) {
                bytes = (long) (Double
                        .parseDouble(lowerSValue.substring(0, lowerSValue.length() - 1)) * ByteSizeUnit.C3);
            } else if (lowerSValue.endsWith("gb")) {
                bytes = (long) (Double
                        .parseDouble(lowerSValue.substring(0, lowerSValue.length() - 2)) * ByteSizeUnit.C3);
            } else if (lowerSValue.endsWith("t")) {
                bytes = (long) (Double
                        .parseDouble(lowerSValue.substring(0, lowerSValue.length() - 1)) * ByteSizeUnit.C4);
            } else if (lowerSValue.endsWith("tb")) {
                bytes = (long) (Double
                        .parseDouble(lowerSValue.substring(0, lowerSValue.length() - 2)) * ByteSizeUnit.C4);
            } else if (lowerSValue.endsWith("p")) {
                bytes = (long) (Double
                        .parseDouble(lowerSValue.substring(0, lowerSValue.length() - 1)) * ByteSizeUnit.C5);
            } else if (lowerSValue.endsWith("pb")) {
                bytes = (long) (Double
                        .parseDouble(lowerSValue.substring(0, lowerSValue.length() - 2)) * ByteSizeUnit.C5);
            } else if (lowerSValue.endsWith("b")) {
                bytes = Long.parseLong(lowerSValue.substring(0, lowerSValue.length() - 1).trim());
            } else if (lowerSValue.equals("-1")) {
                bytes = -1;
            } else if (lowerSValue.equals("0")) {
                bytes = 0;
            } else {
                // Missing units:
                throw new ParseException(
                        "failed to parse setting [" + settingName + "] with value [" + sValue + "] as a size in bytes: unit is missing or unrecognized");
            }
        } catch (NumberFormatException e) {
            throw new ParseException("failed to parse [" + sValue + "]", e);
        }
        return new ByteSizeValue(bytes, ByteSizeUnit.BYTES);
    }

    @Override
    public String toString() {
        long bytes = getBytes();
        double value = bytes;
        String suffix = "b";
        if (bytes >= ByteSizeUnit.C5) {
            value = getPbFraction();
            suffix = "pb";
        } else if (bytes >= ByteSizeUnit.C4) {
            value = getTbFraction();
            suffix = "tb";
        } else if (bytes >= ByteSizeUnit.C3) {
            value = getGbFraction();
            suffix = "gb";
        } else if (bytes >= ByteSizeUnit.C2) {
            value = getMbFraction();
            suffix = "mb";
        } else if (bytes >= ByteSizeUnit.C1) {
            value = getKbFraction();
            suffix = "kb";
        }
        return StringUtilsEx.format1Decimals(value, suffix);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && compareTo((ByteSizeValue) o) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(((double) size) * unit.toBytes(1));
    }

    @Override
    public int compareTo(ByteSizeValue o) {
        double thisValue = ((double) size) * unit.toBytes(1);
        double otherValue = ((double) o.size) * o.unit.toBytes(1);
        return Double.compare(thisValue, otherValue);
    }

    public static class ParseException extends RuntimeException {
        public ParseException() {
        }

        public ParseException(String message) {
            super(message);
        }

        public ParseException(String message, Throwable cause) {
            super(message, cause);
        }

        public ParseException(Throwable cause) {
            super(cause);
        }

        public ParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
