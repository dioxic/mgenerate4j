package uk.dioxic.mgenerate.common.transformer;

import uk.dioxic.mgenerate.common.exception.TransformerException;

public interface Transformer<T> {

    T transform(Object objectToTransform) throws TransformerException;

}
