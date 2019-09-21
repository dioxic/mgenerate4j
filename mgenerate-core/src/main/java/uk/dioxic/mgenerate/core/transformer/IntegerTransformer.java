package uk.dioxic.mgenerate.core.transformer;

import uk.dioxic.mgenerate.common.Transformer;
import uk.dioxic.mgenerate.common.annotation.ValueTransformer;
import uk.dioxic.mgenerate.common.exception.TransformerException;

@ValueTransformer(Double.class)
public class IntegerTransformer implements Transformer<Integer> {

    @Override
    public Integer transform(Object objectToTransform) {
        if (objectToTransform instanceof Number) {
            Number n = (Number)objectToTransform;
            return n.intValue();
        }

        throw new TransformerException(objectToTransform.getClass(), Integer.class);
    }
}
