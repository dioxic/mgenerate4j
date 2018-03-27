package com.dioxic.mgenerate.annotation.processor;

import com.dioxic.mgenerate.annotation.OperatorProperty;
import com.dioxic.mgenerate.annotation.model.FieldModel;
import com.dioxic.mgenerate.operator.Operator;
import com.dioxic.mgenerate.operator.OperatorBuilder;
import com.squareup.javapoet.*;
import org.bson.Document;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.beans.Transient;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BuilderGenerator {

    private final TypeElement typeElement;
    private final String packageName;
    private final String className;
    private final ClassName thisType;

    public BuilderGenerator(TypeElement typeElement) {
        this.typeElement = typeElement;
        this.packageName = Util.elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        this.className = typeElement.getSimpleName() + "Builder";
        this.thisType = ClassName.get(typeElement);
    }

    public CharSequence getFullyQualifiedName() {
        if (this.packageName != null && this.packageName.trim().length() > 0) {
            return this.packageName + "." + this.className;
        }
        return this.className;
    }

    public TypeSpec generate() {

        ClassName builderInterface = ClassName.get(OperatorBuilder.class);

        List<FieldModel> properties = getProperties();

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(ParameterizedTypeName.get(builderInterface, this.thisType));

        addFields(classBuilder, properties);

        addPropertyMethods(classBuilder, properties);

        addDocumentMethod(classBuilder, properties);

        MethodSpec validateMethod = addValidateMethod(classBuilder, properties);

        addBuilderMethod(classBuilder, properties, validateMethod);

        TypeSpec classSpec = classBuilder.build();

        return classSpec;
    }

    public void addFields(TypeSpec.Builder classBuilder, List<FieldModel> properties) {
        for (FieldModel field : properties) {
            classBuilder.addField(Operator.class, field.getName(), Modifier.PRIVATE);
        }
    }

    public String getPackageName() {
        return packageName;
    }

    public List<FieldModel> getProperties() {
        return typeElement
                .getEnclosedElements().stream().filter(this::filterOperatorProperties)
                .map(o -> new FieldModel(o.getSimpleName().toString(), o.getAnnotation(OperatorProperty.class).required()))
                .collect(Collectors.toList());
    }

    private boolean filterOperatorProperties(Element el) {
        return el.getKind() == ElementKind.FIELD
                && !el.getModifiers().contains(Modifier.PRIVATE)
                && el.getAnnotation(OperatorProperty.class) != null;
    }

    public MethodSpec addBuilderMethod(TypeSpec.Builder classBuilder, List<FieldModel> properties, MethodSpec validate) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(this.thisType)
                .addStatement("$N()", validate)
                .addStatement("$T obj = new $T()", thisType, thisType);

        for (FieldModel property : properties) {
            if (property.isRequired()) {
                builder.beginControlFlow("if ($L != null)", property.getName());
            }
            builder.addStatement("obj.$L = $L", property.getName(), property.getName());
            if (property.isRequired()) {
                builder.endControlFlow();
            }
        }

        builder.addStatement("return obj");

        MethodSpec method = builder.build();
        classBuilder.addMethod(method);
        return method;
    }

    public MethodSpec addValidateMethod(TypeSpec.Builder classBuilder, List<FieldModel> properties) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("validate")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(void.class);

        properties.stream().filter(FieldModel::isRequired).forEach(o ->
            builder.addStatement("$T.notNull($S,$L)", org.bson.assertions.Assertions.class, o.getName(), o.getName())
        );

        MethodSpec method = builder.build();
        classBuilder.addMethod(method);
        return method;
    }

    public MethodSpec addDocumentMethod(TypeSpec.Builder classBuilder, List<FieldModel> properties) {
        if (!properties.isEmpty()) {
            MethodSpec.Builder builder = MethodSpec.methodBuilder("document")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addParameter(Document.class, "document")
                    .returns(ClassName.get(packageName, className));

            for (FieldModel property : properties) {
                builder.addStatement("$L = document.get($S, $T.class)", property.getName(), property.getName(), Operator.class);
            }

            builder.addStatement("return this");

            MethodSpec method = builder.build();
            classBuilder.addMethod(method);
            return method;
        }
        return null;
    }

    public void addPropertyMethods(TypeSpec.Builder classBuilder, List<FieldModel> properties) {
        for (FieldModel property : properties) {
            MethodSpec.Builder builder = MethodSpec.methodBuilder(property.getName())
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .returns(ClassName.get(packageName, className))
                    .addParameter(Operator.class, property.getName())
                    .addStatement("this.$L = $L", property.getName(), property.getName())
                    .addStatement("return this");

            classBuilder.addMethod(builder.build());
        }
    }

}
