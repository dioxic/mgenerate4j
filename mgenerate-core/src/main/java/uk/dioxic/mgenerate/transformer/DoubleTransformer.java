package uk.dioxic.mgenerate.transformer;

import uk.dioxic.mgenerate.annotation.ValueTransformer;
import uk.dioxic.mgenerate.exception.TransformerException;

import java.time.LocalDateTime;

@ValueTransformer(Double.class)
public class DoubleTransformer implements Transformer<Double> {

    @Override
    public Double transform(Object objectToTransform) {
        if (objectToTransform instanceof Number) {
            Number n = (Number)objectToTransform;
            return n.doubleValue();
        }

        throw new TransformerException(objectToTransform.getClass(), Double.class);
    }
}
