package uk.dioxic.mgenerate.apt.model;

import uk.dioxic.mgenerate.annotation.PojoProperty;

import javax.lang.model.element.Element;

public class PojoPropertyModel extends AbstractFieldModel {

    private final boolean required;
    private final boolean subDoc;
    private final String docKey;

    public PojoPropertyModel(Element element) {
        super(element.getSimpleName().toString(), element.asType());

        docKey = getKey(element);
        required = element.getAnnotation(PojoProperty.class).required();
        subDoc = element.getAnnotation(PojoProperty.class).subDoc();
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isSubDoc() {
        return subDoc;
    }

    public String getDocKey() {
        return docKey;
    }

    private static String getKey(Element element) {
        String annotationValue = element.getAnnotation(PojoProperty.class).value();
        return annotationValue.isEmpty() ? element.getSimpleName().toString() : annotationValue;
    }

}
