package uk.dioxic.mgenerate.core.transformer;

import uk.dioxic.mgenerate.common.Transformer;
import uk.dioxic.mgenerate.common.annotation.ValueTransformer;
import uk.dioxic.mgenerate.common.exception.TransformerException;

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

        throw new TransformerException(objectToTransform.getClass(), ChronoField.class);
    }
}