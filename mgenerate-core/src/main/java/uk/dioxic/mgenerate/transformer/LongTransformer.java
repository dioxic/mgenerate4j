package uk.dioxic.mgenerate.transformer;

import uk.dioxic.mgenerate.annotation.ValueTransformer;
import uk.dioxic.mgenerate.exception.TransformerException;

@ValueTransformer(Long.class)
public class LongTransformer implements Transformer<Long> {

    @Override
    public Long transform(Object objectToTransform) {
        if (objectToTransform instanceof Number) {
            Number n = (Number)objectToTransform;
            return n.longValue();
        }

        throw new TransformerException(objectToTransform.getClass(), Long.class);
    }
}
