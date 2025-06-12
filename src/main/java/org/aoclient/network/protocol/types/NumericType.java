package org.aoclient.network.protocol.types;

public enum NumericType {

    BYTE(0, 255),
    INTEGER(-32768, 32767),
    LONG(-2147483648L, 2147483647L),
    TRIGGER(0, 6);

    private final long min;
    private final long max;

    NumericType(long min, long max) {
        this.min = min;
        this.max = max;
    }

    public boolean isInRange(long value) {
        return value >= min && value <= max;
    }

}
