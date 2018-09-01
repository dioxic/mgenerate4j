package uk.dioxic.mgenerate.core.transformer;

import uk.dioxic.mgenerate.common.annotation.ValueTransformer;
import uk.dioxic.mgenerate.common.exception.TransformerException;
import uk.dioxic.mgenerate.common.transformer.Transformer;

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
