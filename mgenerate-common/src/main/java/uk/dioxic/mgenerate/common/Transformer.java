package uk.dioxic.mgenerate.common;

import uk.dioxic.mgenerate.common.exception.TransformerException;

@FunctionalInterface
public interface Transformer<T> {

    T transform(Object objectToTransform) throws TransformerException;

}
