package uk.dioxic.mgenerate.core.transformer;

import uk.dioxic.mgenerate.common.Transformer;
import uk.dioxic.mgenerate.common.annotation.ValueTransformer;
import uk.dioxic.mgenerate.common.exception.TransformerException;
import uk.dioxic.mgenerate.core.operator.type.AgeType;

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