package com.dioxic.mgenerate.operator;

public class Wrapper<T> implements Operator<T> {

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
