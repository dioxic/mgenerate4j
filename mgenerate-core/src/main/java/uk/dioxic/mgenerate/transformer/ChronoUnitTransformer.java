package uk.dioxic.mgenerate.transformer;

import uk.dioxic.mgenerate.annotation.ValueTransformer;
import uk.dioxic.mgenerate.exception.TransformerException;

import java.time.temporal.ChronoUnit;

@ValueTransformer(ChronoUnit.class)
public class ChronoUnitTransformer implements Transformer<ChronoUnit> {
    @Override
    public ChronoUnit transform(Object objectToTransform) {
        if (objectToTransform instanceof ChronoUnit) {
            return (ChronoUnit) objectToTransform;
        }

        if (objectToTransform instanceof String) {
            return ChronoUnit.valueOf(((String) objectToTransform).toUpperCase());
        }

        throw new TransformerException(objectToTransform.getClass(), ChronoUnit.class);
    }
}