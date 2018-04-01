package com.dioxic.mgenerate.transformer;

import com.dioxic.mgenerate.Transformer;
import com.dioxic.mgenerate.annotation.ValueTransformer;
import com.dioxic.mgenerate.operator.person.AgeType;

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

        throw new IllegalStateException("could not convert " + objectToTransform.getClass().getSimpleName() + " to AgeType");
    }
}