package com.dioxic.mgenerate.annotation.processor;

import com.dioxic.mgenerate.annotation.OperatorBuilderClass;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import com.dioxic.mgenerate.annotation.model.FieldModel;
import com.dioxic.mgenerate.operator.*;
import com.squareup.javapoet.*;
import org.bson.Document;
import org.bson.assertions.Assertions;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
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

    public TypeSpec generate(Appendable appendable) throws IOException, ClassNotFoundException {
        ClassName builderInterface = ClassName.get(OperatorBuilder.class);
        List<FieldModel> properties = getProperties();

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(ParameterizedTypeName.get(builderInterface, this.thisType))
                .addAnnotation(AnnotationSpec.builder(OperatorBuilderClass.class)
                        .addMember("value", "$S", getOperatorKey())
                        .build());

        addFields(classBuilder, properties);

        addPropertyMethods(classBuilder, properties);

        addDocumentMethod(classBuilder, properties);

        MethodSpec validateMethod = addValidateMethod(classBuilder, properties);

        addBuildMethod(classBuilder, properties, validateMethod);

        TypeSpec classSpec = classBuilder.build();

        JavaFile javaFile = JavaFile.builder(this.packageName, classSpec).build();
        javaFile.writeTo(appendable);

        return classSpec;
    }

    public void addFields(TypeSpec.Builder classBuilder, List<FieldModel> properties) {
        for (FieldModel field : properties) {
            classBuilder.addField(field.getOperatorTypeName(), field.getName(), Modifier.PRIVATE);
        }
    }

    public String getPackageName() {
        return packageName;
    }

    public List<FieldModel> getProperties() {
        return typeElement
                .getEnclosedElements().stream().filter(this::filterOperatorProperties)
                .map(FieldModel::new)
                .collect(Collectors.toList());
    }

    private boolean filterOperatorProperties(Element el) {
        return el.getKind() == ElementKind.FIELD
                && !el.getModifiers().contains(Modifier.PRIVATE)
                && el.getAnnotation(OperatorProperty.class) != null;
    }

    public void addBuildMethod(TypeSpec.Builder classBuilder, List<FieldModel> properties, MethodSpec validate) throws ClassNotFoundException {

        CodeBlock.Builder requiredBlock = CodeBlock.builder();

        for (FieldModel property : properties) {
            if (!property.isRequired()) {
                requiredBlock.beginControlFlow("if ($L != null)", property.getName());
            }

            if (!property.isOperatorType()) {
                requiredBlock.addStatement("obj.$L = $L.resolve()",
                        property.getName(),
                        property.getName());
            }
            else {
                requiredBlock.addStatement("obj.$L = $L", property.getName(), property.getName());
            }

            if (!property.isRequired()) {
                requiredBlock.endControlFlow();
            }
        }

        CodeBlock.Builder initBlock = CodeBlock.builder();

        if (typeElement.getInterfaces().stream().filter(clazz -> Util.isSameType(clazz, Initializable.class)).count() > 0) {
            initBlock.addStatement("obj.initialize()");
        }

        MethodSpec method = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(this.thisType)
                .addStatement("$N()", validate)
                .addStatement("$T obj = new $T()", thisType, thisType)
                .addCode(requiredBlock.build())
                .addCode(initBlock.build())
                .addStatement("return obj")
                .build();

        classBuilder.addMethod(method);
    }

    @Deprecated
    public void addKeyMethod(TypeSpec.Builder classBuilder) {
        String operatorKey = typeElement.getAnnotation(OperatorClass.class).value();

        if (operatorKey.isEmpty()) {
            char[] key = typeElement.getSimpleName().toString().toCharArray();
            key[0] = Character.toLowerCase(key[0]);
            operatorKey = String.valueOf(key);
        }

        MethodSpec method = MethodSpec.methodBuilder("getKey")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(String.class)
                .addStatement("return $S", operatorKey)
                .build();

        classBuilder.addMethod(method);
    }

    private String getOperatorKey() {
        String operatorKey = typeElement.getAnnotation(OperatorClass.class).value();

        if (operatorKey.isEmpty()) {
            char[] key = typeElement.getSimpleName().toString().toCharArray();
            key[0] = Character.toLowerCase(key[0]);
            operatorKey = String.valueOf(key);
        }

        return operatorKey;
    }

    public MethodSpec addValidateMethod(TypeSpec.Builder classBuilder, List<FieldModel> properties) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("validate")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(void.class);

        properties.stream().filter(FieldModel::isRequired).forEach(o ->
                builder.addStatement("$T.notNull($S,$L)", Assertions.class, o.getName(), o.getName())
        );

        MethodSpec method = builder.build();
        classBuilder.addMethod(method);
        return method;
    }

    public void addDocumentMethod(TypeSpec.Builder classBuilder, List<FieldModel> properties) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("document")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(Document.class, "document")
                .returns(ClassName.get(packageName, className));

        for (FieldModel property : properties) {
            if (property.isEnumRootType()) {
                TypeSpec enumWrapper = TypeSpec.anonymousClassBuilder("$N", property.getName() + "Name")
                        .addSuperinterface(ParameterizedTypeName.get(ClassName.get(EnumWrapper.class), property.getRootTypeName()))
                        .addMethod(MethodSpec.methodBuilder("resolve")
                                .addAnnotation(Override.class)
                                .addModifiers(Modifier.PUBLIC)
                                .returns(property.getRootTypeName())
                                .addStatement("return $T.valueOf(name.resolve().toUpperCase())", property.getRootTypeName())
                                .build())
                        .build();


                builder.addStatement("$T $N = document.get($S, $T.class)",
                        ParameterizedTypeName.get(Operator.class, String.class),
                        property.getName() + "Name",
                        property.getName(),
                        Operator.class)
                        .addStatement("$N = $L", property.getName(), enumWrapper);
            }
            else {
                builder.addStatement("$L = document.get($S, $T.class)", property.getName(), property.getName(), Operator.class);
            }
        }

        builder.addStatement("return this");

        classBuilder.addMethod(builder.build());
    }

    public void addPropertyMethods(TypeSpec.Builder classBuilder, List<FieldModel> properties) {
        for (FieldModel property : properties) {
            classBuilder.addMethod(MethodSpec.methodBuilder(property.getName())
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .returns(ClassName.get(packageName, className))
                    .addParameter(property.getOperatorTypeName(), property.getName())
                    .addStatement("this.$L = $L", property.getName(), property.getName())
                    .addStatement("return this")
                    .build());

            classBuilder.addMethod(MethodSpec.methodBuilder(property.getName())
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .returns(ClassName.get(packageName, className))
                    .addParameter(property.getRootTypeName(), property.getName())
                    .addStatement("this.$L = new $T($L)", property.getName(), Wrapper.class, property.getName())
                    .addStatement("return this")
                    .build());

        }
    }

}
