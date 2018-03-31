package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.Resolvable;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class DateWrapper implements Resolvable<LocalDateTime> {

    private static final DateTimeFormatter DFT = DateTimeFormatter.ISO_DATE_TIME;
    //private static final DateTimeFormatter DFT = DateTimeFormatter.ISO;

    private final Resolvable<String> operator;

    public DateWrapper(Resolvable<String> operator) {
        this.operator = operator;
    }

    @Override
    public LocalDateTime resolve() {
        String date = operator.resolve();

        if (date.contains("T")) {
            return LocalDateTime.parse(operator.resolve(), DFT);
        }

        return LocalDateTime.now();

    }
}
