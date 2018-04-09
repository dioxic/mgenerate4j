package uk.dioxic.mgenerate.transformer;

import uk.dioxic.mgenerate.annotation.ValueTransformer;

import java.time.temporal.ChronoField;

@ValueTransformer(ChronoField.class)
public class ChronoFieldTransformer implements Transformer<ChronoField> {
    @Override
    public ChronoField transform(Object objectToTransform) {
        if (objectToTransform instanceof ChronoField) {
            return (ChronoField) objectToTransform;
        }

        if (objectToTransform instanceof String) {
            return ChronoField.valueOf(((String) objectToTransform).toUpperCase());
        }

        throw new IllegalStateException("could not convert " + objectToTransform.getClass().getSimpleName() + " to ChronoField");
    }
}