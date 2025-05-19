package com.example.surfspot.Enum;

public enum SurfBreak {
    OUTER_BANKS("outer banks"),
    REEF_BREAK("reef break"),
    BEACH_BREAK("beach break"),
    POINT_BREAK("point break");

    private final String value;

    SurfBreak(String value) {
        this.value =  value;
    }

    public String getValue() {
        return this.value;
    }
}
