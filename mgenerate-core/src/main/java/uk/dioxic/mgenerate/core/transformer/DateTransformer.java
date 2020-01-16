package uk.dioxic.mgenerate.core.transformer;

import uk.dioxic.mgenerate.common.Transformer;
import uk.dioxic.mgenerate.common.annotation.ValueTransformer;
import uk.dioxic.mgenerate.common.exception.TransformerException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@ValueTransformer(LocalDateTime.class)
public class DateTransformer implements Transformer<LocalDateTime> {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter DT = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDateTime transform(Object objectToTransform) throws TransformerException {
        if (objectToTransform == null) {
            return null;
        }

        if (objectToTransform instanceof LocalDateTime){
            return (LocalDateTime)objectToTransform;
        }

        if (objectToTransform instanceof String) {
            try {
                String dateString = (String) objectToTransform;
                if (dateString.contains("T")) {
                    return LocalDateTime.parse(dateString, DTF);
                }

                return LocalDateTime.of(LocalDate.parse(dateString, DT), LocalTime.MIN);
            }
            catch (DateTimeParseException e) {
                throw new TransformerException(e);
            }
        }

        throw new TransformerException(objectToTransform.getClass(), LocalDateTime.class);
    }
}
