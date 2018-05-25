package uk.dioxic.mgenerate.transformer;

import uk.dioxic.mgenerate.annotation.ValueTransformer;
import uk.dioxic.mgenerate.exception.TransformerException;
import uk.dioxic.mgenerate.operator.Hash;

@ValueTransformer(Hash.HashOutput.class)
public class HashOutputTransformer implements Transformer<Hash.HashOutput> {
    @Override
    public Hash.HashOutput transform(Object objectToTransform) {
        if (objectToTransform instanceof Hash.HashOutput) {
            return (Hash.HashOutput) objectToTransform;
        }

        if (objectToTransform instanceof String) {
            return Hash.HashOutput.valueOf(((String) objectToTransform).toUpperCase());
        }

        throw new TransformerException(objectToTransform.getClass(), Hash.HashOutput.class);
    }
}