package com.example.surfspot.Enum;

public enum SurfBreak {
    OUTER_BANKS("Outer Banks"),
    REEF_BREAK("Reef Break"),
    BEACH_BREAK("Beach Break"),
    POINT_BREAK("Point Break");

    private final String value;

    SurfBreak(String value) {
        this.value =  value;
    }

    public String getValue() {
        return this.value;
    }
}
