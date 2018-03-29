package com.dioxic.mgenerate.operator;

public abstract class EnumWrapper<T extends Enum> implements Operator<T> {

    protected Operator<String> name;

    public EnumWrapper(Operator<String> name) {
        this.name = name;
    }

    @Override
    public abstract T resolve();
}
