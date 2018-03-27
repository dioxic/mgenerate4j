package com.dioxic.mgenerate.operator;

public class Wrapper implements Operator {

    private Object value;

    public Wrapper(Object value) {
        this.value = value;
    }

    @Override
    public Object resolve() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
