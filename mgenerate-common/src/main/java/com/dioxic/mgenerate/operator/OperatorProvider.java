package com.dioxic.mgenerate.operator;

public interface OperatorProvider {

    OperatorBuilder get(String key);

    boolean provides(String key);
}
