package cn.howardliu.gear.monitor.unit;

/**
 * <br>created at 16-12-27
 *
 * @author liuxh
 * @since 1.0.2
 */
public enum ByteSizeUnit {
    BYTES {
        @Override
        public long toBytes(long size) {
            return size;
        }

        @Override
        public long toKB(long size) {
            return size >> 10;
        }

        @Override
        public long toMB(long size) {
            return size >> 10 >> 10;
        }

        @Override
        public long toGB(long size) {
            return size >> 10 >> 10 >> 10;
        }

        @Override
        public long toTB(long size) {
            return size >> 10 >> 10 >> 10 >> 10;
        }

        @Override
        public long toPB(long size) {
            return size >> 10 >> 10 >> 10 >> 10 >> 10;
        }
    },
    KB {
        @Override
        public long toBytes(long size) {
            return x(size, C1, MAX / C1);
        }

        @Override
        public long toKB(long size) {
            return size;
        }

        @Override
        public long toMB(long size) {
            return size >> 10;
        }

        @Override
        public long toGB(long size) {
            return size >> 10 >> 10;
        }

        @Override
        public long toTB(long size) {
            return size >> 10 >> 10 >> 10;
        }

        @Override
        public long toPB(long size) {
            return size >> 10 >> 10 >> 10 >> 10;
        }
    },
    MB {
        @Override
        public long toBytes(long size) {
            return x(size, C2, MAX / C2);
        }

        @Override
        public long toKB(long size) {
            return x(size, C1, MAX / C1);
        }

        @Override
        public long toMB(long size) {
            return size;
        }

        @Override
        public long toGB(long size) {
            return size >> 10;
        }

        @Override
        public long toTB(long size) {
            return size >> 10 >> 10;
        }

        @Override
        public long toPB(long size) {
            return size >> 10 >> 10 >> 10;
        }
    },
    GB {
        @Override
        public long toBytes(long size) {
            return x(size, C3, MAX / C3);
        }

        @Override
        public long toKB(long size) {
            return x(size, C2, MAX / C2);
        }

        @Override
        public long toMB(long size) {
            return x(size, C1, MAX / C1);
        }

        @Override
        public long toGB(long size) {
            return size;
        }

        @Override
        public long toTB(long size) {
            return size >> 10;
        }

        @Override
        public long toPB(long size) {
            return size >> 10 >> 10;
        }
    },
    TB {
        @Override
        public long toBytes(long size) {
            return x(size, C4, MAX / C4);
        }

        @Override
        public long toKB(long size) {
            return x(size, C3, MAX / C3);
        }

        @Override
        public long toMB(long size) {
            return x(size, C2, MAX / C2);
        }

        @Override
        public long toGB(long size) {
            return x(size, C1, MAX / C1);
        }

        @Override
        public long toTB(long size) {
            return size;
        }

        @Override
        public long toPB(long size) {
            return size >> 10;
        }
    },
    PB {
        @Override
        public long toBytes(long size) {
            return x(size, C5, MAX / C5);
        }

        @Override
        public long toKB(long size) {
            return x(size, C4, MAX / C4);
        }

        @Override
        public long toMB(long size) {
            return x(size, C3, MAX / C3);
        }

        @Override
        public long toGB(long size) {
            return x(size, C2, MAX / C2);
        }

        @Override
        public long toTB(long size) {
            return x(size, C1, MAX / C1);
        }

        @Override
        public long toPB(long size) {
            return size;
        }
    };

    static final long C0 = 1L;
    static final long C1 = C0 << 10;
    static final long C2 = C1 << 10;
    static final long C3 = C2 << 10;
    static final long C4 = C3 << 10;
    static final long C5 = C4 << 10;

    static final long MIN = Long.MIN_VALUE;
    static final long MAX = Long.MAX_VALUE;

    public abstract long toBytes(long size);

    public abstract long toKB(long size);

    public abstract long toMB(long size);

    public abstract long toGB(long size);

    public abstract long toTB(long size);

    public abstract long toPB(long size);

    public static ByteSizeUnit fromId(int id) {
        if (id < 0 || id >= values().length) {
            throw new IllegalArgumentException("No byte size unit found for id [" + id + "]");
        }
        return values()[id];
    }

    static long x(long d, long m, long over) {
        if (d > over) {
            return MAX;
        }
        if (d < -over) {
            return MIN;
        }
        return d * m;
    }
}
