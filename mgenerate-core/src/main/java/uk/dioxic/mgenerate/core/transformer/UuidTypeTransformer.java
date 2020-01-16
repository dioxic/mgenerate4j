package uk.dioxic.mgenerate.core.transformer;

import uk.dioxic.mgenerate.common.Transformer;
import uk.dioxic.mgenerate.common.annotation.ValueTransformer;
import uk.dioxic.mgenerate.common.exception.TransformerException;
import uk.dioxic.mgenerate.core.operator.type.UuidType;

@ValueTransformer(UuidType.class)
public class UuidTypeTransformer implements Transformer<UuidType> {
    @Override
    public UuidType transform(Object objectToTransform) {
        if (objectToTransform instanceof UuidType) {
            return (UuidType) objectToTransform;
        }

        if (objectToTransform instanceof String) {
            return UuidType.valueOf(((String) objectToTransform).toUpperCase());
        }

        throw new TransformerException(objectToTransform.getClass(), UuidType.class);
    }

}