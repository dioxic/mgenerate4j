package com.dioxic.mgenerate.operator.person;

import java.time.LocalDateTime;

public enum AgeType {
    CHILD(1, 13),
    TEEN(13, 18),
    ADULT(18, 60),
    SENIOR(60, 120),
    DEFAULT(1, 120);

    AgeType(int min, int max) {
        this.min = min;
        this.max = max;
        minBirthday = LocalDateTime.now().minusYears(max);
        maxBirthday = LocalDateTime.now().minusYears(min);
    }

    private final int min;
    private final int max;
    private final LocalDateTime minBirthday;
    private final LocalDateTime maxBirthday;

    public int getMinAge() {
        return min;
    }

    public int getMaxAge() {
        return max;
    }

    public LocalDateTime getMinBirthday() {
        return minBirthday;
    }

    public LocalDateTime getMaxBirthday() {
        return maxBirthday;
    }
}