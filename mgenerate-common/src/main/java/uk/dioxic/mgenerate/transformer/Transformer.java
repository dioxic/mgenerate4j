package uk.dioxic.mgenerate.transformer;

import uk.dioxic.mgenerate.exception.TransformerException;

public interface Transformer<T> {

    T transform(Object objectToTransform) throws TransformerException;

}
