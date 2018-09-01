package uk.dioxic.mgenerate.apt.model;

import uk.dioxic.mgenerate.common.annotation.OperatorProperty;

import javax.lang.model.element.Element;

public class OperatorPropertyModel extends AbstractFieldModel {

    private final boolean required;

    public OperatorPropertyModel(Element element) {
        super(element.getSimpleName().toString(),
                element.asType());

        required = element.getAnnotation(OperatorProperty.class).required();
    }

    public boolean isRequired() {
        return required;
    }

}
