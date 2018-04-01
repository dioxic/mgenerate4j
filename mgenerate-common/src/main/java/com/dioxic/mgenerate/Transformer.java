package com.dioxic.mgenerate;

public interface Transformer<T> {

    T transform(Object objectToTransform);

}
