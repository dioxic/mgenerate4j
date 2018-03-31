package com.dioxic.mgenerate.transformer;

import com.dioxic.mgenerate.annotation.ValueTransformer;
import org.bson.Transformer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ValueTransformer(LocalDateTime.class)
public class DateTransformer implements Transformer {

    private static final DateTimeFormatter DFT = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public LocalDateTime transform(Object objectToTransform) {
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
