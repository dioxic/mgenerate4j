package com.dioxic.mgenerate;

public interface OperatorProvider {

    OperatorBuilder get(String key);

    boolean provides(String key);
}
