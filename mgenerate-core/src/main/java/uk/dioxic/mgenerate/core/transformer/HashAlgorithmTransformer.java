package uk.dioxic.mgenerate.core.transformer;

import uk.dioxic.mgenerate.common.Transformer;
import uk.dioxic.mgenerate.common.annotation.ValueTransformer;
import uk.dioxic.mgenerate.common.exception.TransformerException;
import uk.dioxic.mgenerate.core.operator.type.HashAlgorithm;
import uk.dioxic.mgenerate.core.operator.type.HashAlgorithm;

@ValueTransformer(HashAlgorithm.class)
public class HashAlgorithmTransformer implements Transformer<HashAlgorithm> {
    @Override
    public HashAlgorithm transform(Object objectToTransform) {
        if (objectToTransform instanceof HashAlgorithm) {
            return (HashAlgorithm) objectToTransform;
        }

        if (objectToTransform instanceof String) {
            return HashAlgorithm.valueOf(((String) objectToTransform).toUpperCase());
        }

        throw new TransformerException(objectToTransform.getClass(), HashAlgorithm.class);
    }
}