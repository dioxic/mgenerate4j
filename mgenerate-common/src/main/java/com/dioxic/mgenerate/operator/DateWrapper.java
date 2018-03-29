package com.dioxic.mgenerate.operator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class DateWrapper implements Operator<LocalDateTime> {

    private static final DateTimeFormatter DFT = DateTimeFormatter.ISO_DATE_TIME;
    //private static final DateTimeFormatter DFT = DateTimeFormatter.ISO;

    private final Operator<String> operator;

    public DateWrapper(Operator<String> operator) {
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
