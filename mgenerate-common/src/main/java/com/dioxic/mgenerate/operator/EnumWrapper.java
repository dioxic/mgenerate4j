package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.Resolvable;

public abstract class EnumWrapper<T extends Enum> implements Resolvable<T> {

    protected final Resolvable<String> name;

    public EnumWrapper(Resolvable<String> name) {
        this.name = name;
    }

    @Override
    public abstract T resolve();
}
