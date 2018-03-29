package com.dioxic.mgenerate.operator;

import org.bson.types.MinKey;

public class Wrapper implements Operator {

    private Object value;

    public Wrapper(Object value) {
        this.value = value;
    }

    @Override
    public MinKey resolve() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
