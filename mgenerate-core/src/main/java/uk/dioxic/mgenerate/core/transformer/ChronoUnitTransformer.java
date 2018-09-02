package uk.dioxic.mgenerate.core.transformer;

import uk.dioxic.mgenerate.common.annotation.ValueTransformer;
import uk.dioxic.mgenerate.common.exception.TransformerException;
import uk.dioxic.mgenerate.common.Transformer;

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