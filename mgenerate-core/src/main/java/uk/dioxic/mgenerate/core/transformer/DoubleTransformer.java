package uk.dioxic.mgenerate.core.transformer;

import uk.dioxic.mgenerate.common.Transformer;
import uk.dioxic.mgenerate.common.annotation.ValueTransformer;
import uk.dioxic.mgenerate.common.exception.TransformerException;

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
