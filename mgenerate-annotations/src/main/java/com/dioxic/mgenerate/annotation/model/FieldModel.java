package com.dioxic.mgenerate.annotation.model;

public class FieldModel {

    private final String name;
    private final boolean required;

    public FieldModel(String name, boolean required) {
        this.name = name;
        this.required = required;
    }

    public FieldModel(String name) {
        this(name, false);
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }
}
