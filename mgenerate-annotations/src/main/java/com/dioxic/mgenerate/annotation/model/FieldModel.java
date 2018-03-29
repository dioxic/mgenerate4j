package com.dioxic.mgenerate.annotation.model;

import com.dioxic.mgenerate.annotation.OperatorProperty;
import com.dioxic.mgenerate.annotation.processor.Util;
import com.dioxic.mgenerate.operator.Operator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.naming.spi.ObjectFactory;
import java.util.List;

public class FieldModel {

    private final String name;
    private final boolean required;
    private final TypeMirror type;
    private List<? extends TypeMirror> typeParameters;

    public FieldModel(Element element) {
        this.name = element.getSimpleName().toString();
        this.required = element.getAnnotation(OperatorProperty.class).required();
        this.type = element.asType();

        if (type.getKind() == TypeKind.DECLARED) {
            typeParameters = ((DeclaredType) element.asType()).getTypeArguments();
        }
        //this(element.getSimpleName().toString(), element.getAnnotation(OperatorProperty.class).required(), element.asType(), ((TypeElement)element).getTypeParameters());
    }

//    public FieldModel(String name, boolean required, TypeMirror type) {
//        this.name = name;
//        this.required = required;
//        this.type = type;
//    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public TypeMirror getType() {
        return type;
    }

    public boolean isOperatorType() {
        return Util.isSameType(type, Operator.class);
    }

    public TypeName getOperatorType() {
        if (isOperatorType()) {
            return ParameterizedTypeName.get(ClassName.get(Operator.class), getRootFieldType());
        }
        else {
            return ParameterizedTypeName.get(ClassName.get(Operator.class), TypeName.get(type));
        }
    }

    public TypeName getRootFieldType() {
        if (isOperatorType()) {
            if (!typeParameters.isEmpty()) {
                return ClassName.get(typeParameters.get(0));
            }
            return ClassName.get(Object.class);
        }
        return TypeName.get(type);
    }
}
