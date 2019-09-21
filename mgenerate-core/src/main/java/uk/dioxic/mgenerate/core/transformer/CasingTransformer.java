package uk.dioxic.mgenerate.core.transformer;

import uk.dioxic.mgenerate.common.Transformer;
import uk.dioxic.mgenerate.common.annotation.ValueTransformer;
import uk.dioxic.mgenerate.common.exception.TransformerException;
import uk.dioxic.mgenerate.core.operator.type.Casing;

import java.util.Locale;

@ValueTransformer(Casing.class)
public class CasingTransformer implements Transformer<Casing> {
    @Override
    public Casing transform(Object objectToTransform) {
        if (objectToTransform instanceof Casing) {
            return (Casing) objectToTransform;
        }

        if (objectToTransform instanceof String) {
            return Casing.valueOf(((String) objectToTransform).toUpperCase());
        }

        throw new TransformerException(objectToTransform.getClass(), Locale.class);
    }
}