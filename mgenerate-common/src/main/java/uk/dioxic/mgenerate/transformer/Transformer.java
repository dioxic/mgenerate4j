package uk.dioxic.mgenerate.transformer;

public interface Transformer<T> {

    T transform(Object objectToTransform);

}
