package uk.dioxic.mgenerate.transformer;

import uk.dioxic.mgenerate.annotation.ValueTransformer;
import uk.dioxic.mgenerate.exception.TransformerException;
import uk.dioxic.mgenerate.operator.person.AgeType;

@ValueTransformer(AgeType.class)
public class AgeTypeTransformer implements Transformer<AgeType> {
    @Override
    public AgeType transform(Object objectToTransform) {
        if (objectToTransform instanceof AgeType) {
            return (AgeType) objectToTransform;
        }

        if (objectToTransform instanceof String) {
            return AgeType.valueOf(((String) objectToTransform).toUpperCase());
        }

        throw new TransformerException(objectToTransform.getClass(), AgeType.class);
    }
}