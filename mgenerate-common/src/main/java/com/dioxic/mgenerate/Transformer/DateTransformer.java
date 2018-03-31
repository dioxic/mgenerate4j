package com.dioxic.mgenerate.Transformer;

import org.bson.Transformer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTransformer implements Transformer {

    private static final DateTimeFormatter DFT = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public Object transform(Object objectToTransform) {
        if (objectToTransform instanceof LocalDateTime){
            return (LocalDateTime)objectToTransform;
        }

        if (objectToTransform instanceof String) {
            String dateString = (String)objectToTransform;
            if (dateString.contains("T")) {
                return LocalDateTime.parse(dateString, DFT);
            }

            return LocalDateTime.now();
        }

        throw new IllegalStateException("could not convert " + objectToTransform.getClass().getSimpleName() + " to LocalDateTime");
    }
}
