package uk.dioxic.mgenerate.transformer;

import uk.dioxic.mgenerate.transformer.Transformer;
import uk.dioxic.mgenerate.annotation.ValueTransformer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ValueTransformer(LocalDateTime.class)
public class DateTransformer implements Transformer<LocalDateTime> {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ISO_DATE_TIME;
    private static final DateTimeFormatter DT = DateTimeFormatter.ISO_DATE;

    @Override
    public LocalDateTime transform(Object objectToTransform) {

        if (objectToTransform instanceof LocalDateTime){
            return (LocalDateTime)objectToTransform;
        }

        if (objectToTransform instanceof String) {
            String dateString = (String)objectToTransform;
            if (dateString.contains("T")) {
                return LocalDateTime.parse(dateString, DTF);
            }

            return LocalDateTime.parse(dateString, DT);
        }

        throw new IllegalStateException("could not convert " + objectToTransform.getClass().getSimpleName() + " to LocalDateTime");
    }
}
