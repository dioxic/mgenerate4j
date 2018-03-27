package com.dioxic.mgenerate.annotation.processor;

import com.dioxic.mgenerate.annotation.model.FieldModel;
import com.dioxic.mgenerate.operator.OperatorBuilder;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.List;

public class ProviderGenerator {

    private final List<TypeSpec> builderSpecs;

    public ProviderGenerator(List<TypeSpec> builderSpecs) {
        this.builderSpecs =  builderSpecs;
    }

    public void generate() {

        ClassName builderInterface = ClassName.get(OperatorBuilder.class);


        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(ParameterizedTypeName.get(builderInterface, this.thisType));
    }

}
