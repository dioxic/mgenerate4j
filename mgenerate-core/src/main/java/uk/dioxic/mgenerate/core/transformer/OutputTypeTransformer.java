package uk.dioxic.mgenerate.core.transformer;

import uk.dioxic.mgenerate.common.Transformer;
import uk.dioxic.mgenerate.common.annotation.ValueTransformer;
import uk.dioxic.mgenerate.common.exception.TransformerException;
import uk.dioxic.mgenerate.core.operator.type.AgeType;
import uk.dioxic.mgenerate.core.operator.type.OutputType;

@ValueTransformer(OutputType.class)
public class OutputTypeTransformer implements Transformer<OutputType> {
    @Override
    public OutputType transform(Object objectToTransform) {
        if (objectToTransform instanceof OutputType) {
            return (OutputType) objectToTransform;
        }

        if (objectToTransform instanceof String) {
            return OutputType.valueOf(((String) objectToTransform).toUpperCase());
        }

        throw new TransformerException(objectToTransform.getClass(), OutputType.class);
    }

}