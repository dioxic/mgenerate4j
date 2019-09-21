package uk.dioxic.mgenerate.core.transformer;

import uk.dioxic.mgenerate.common.Transformer;
import uk.dioxic.mgenerate.common.annotation.ValueTransformer;
import uk.dioxic.mgenerate.common.exception.TransformerException;
import uk.dioxic.mgenerate.core.operator.type.DateDisplayType;

@ValueTransformer(DateDisplayType.class)
public class DateDisplayTypeTransformer implements Transformer<DateDisplayType> {
    @Override
    public DateDisplayType transform(Object objectToTransform) {
        if (objectToTransform instanceof DateDisplayType) {
            return (DateDisplayType) objectToTransform;
        }

        if (objectToTransform instanceof String) {
            return DateDisplayType.valueOf(((String) objectToTransform).toUpperCase());
        }

        throw new TransformerException(objectToTransform.getClass(), DateDisplayType.class);
    }
}