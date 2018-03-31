package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.Resolvable;

public class Wrapper<T> implements Resolvable<T> {

    private final T value;

    public Wrapper(T value) {
        this.value = value;
    }

    @Override
    public T resolve() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
