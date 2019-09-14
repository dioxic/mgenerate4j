package uk.dioxic.mgenerate.apt.model;

import uk.dioxic.mgenerate.common.annotation.OperatorProperty;

import javax.lang.model.element.Element;

public class OperatorPropertyModel extends AbstractFieldModel {

    private final boolean required;
    private final boolean primary;

    public OperatorPropertyModel(Element element) {
        super(element.getSimpleName().toString(),
                element.asType());

        required = element.getAnnotation(OperatorProperty.class).required();
        primary = element.getAnnotation(OperatorProperty.class).primary();
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isPrimary() {
        return primary;
    }

}
