package uk.dioxic.mgenerate.core.transformer;

import uk.dioxic.mgenerate.common.Transformer;
import uk.dioxic.mgenerate.common.annotation.ValueTransformer;
import uk.dioxic.mgenerate.common.exception.TransformerException;
import uk.dioxic.mgenerate.core.operator.type.HashType;

@ValueTransformer(HashType.class)
public class HashOutputTransformer implements Transformer<HashType> {
    @Override
    public HashType transform(Object objectToTransform) {
        if (objectToTransform instanceof HashType) {
            return (HashType) objectToTransform;
        }

        if (objectToTransform instanceof String) {
            return HashType.valueOf(((String) objectToTransform).toUpperCase());
        }

        throw new TransformerException(objectToTransform.getClass(), HashType.class);
    }
}