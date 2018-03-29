package com.dioxic.mgenerate.annotation.processor;

import com.dioxic.mgenerate.operator.OperatorBuilder;
import com.dioxic.mgenerate.operator.OperatorProvider;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProviderGenerator {

    private final List<TypeSpec> builderSpecs;
    private final String className;
    private final String packageName;

    public ProviderGenerator(String packageName, String className, List<TypeSpec> builderSpecs) {
        this.builderSpecs = builderSpecs;
        this.className = className;
        this.packageName = packageName;
    }

    public TypeSpec generate(Appendable appendable) throws IOException {

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(OperatorProvider.class);

        addStatics(classBuilder);
        addInstanceMethod(classBuilder);
        addConstructor(classBuilder);
        addAddMethod(classBuilder);
        addCanProviderMethod(classBuilder);
        addGetMethod(classBuilder);

        TypeSpec classSpec = classBuilder.build();

        JavaFile javaFile = JavaFile.builder(packageName, classSpec).build();
        javaFile.writeTo(appendable);

        return classSpec;
    }

    private void addStatics(TypeSpec.Builder classBuilder) {
        CodeBlock.Builder builder = CodeBlock.builder()
                .addStatement("registry = new $T()", ParameterizedTypeName.get(HashMap.class, String.class, OperatorBuilder.class))
                .addStatement("instance = new $T()", ClassName.get(packageName, className));

        for (TypeSpec builderSpec : builderSpecs) {
            builder.addStatement("add(new $N())", builderSpec);
        }

        classBuilder.addField(ParameterizedTypeName.get(Map.class, String.class, OperatorBuilder.class), "registry", Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC);
        classBuilder.addField(ClassName.get(packageName, className), "instance", Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC);
        classBuilder.addStaticBlock(builder.build());

    }

    private void addInstanceMethod(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("instance")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(OperatorProvider.class)
                .addStatement("return instance");

        classBuilder.addMethod(builder.build());
    }

    private void addCanProviderMethod(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("provides")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "key")
                .returns(boolean.class)
                .addStatement("return registry.containsKey($N)", "key");

        classBuilder.addMethod(builder.build());
    }

    private void addGetMethod(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "key")
                .returns(OperatorBuilder.class)
                .addStatement("return registry.get($N)", "key");

        classBuilder.addMethod(builder.build());
    }

    private void addAddMethod(TypeSpec.Builder classBuilder) {
        String param = "operatorBuilder";

        MethodSpec.Builder builder = MethodSpec.methodBuilder("add")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .addParameter(OperatorBuilder.class, param)
                .addStatement("registry.put($N.getKey(), $N)", param, param);

        classBuilder.addMethod(builder.build());
    }

    private void addConstructor(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE);
//                .addStatement("registry = new $T()", ParameterizedTypeName.get(HashMap.class, String.class, OperatorBuilder.class));
//
//        for (TypeSpec builderSpec : builderSpecs) {
//            builder.addStatement("add(new $N())", builderSpec);
//        }
//
//        classBuilder.addField(ParameterizedTypeName.get(Map.class, String.class, OperatorBuilder.class), "registry", Modifier.PRIVATE, Modifier.FINAL);

        classBuilder.addMethod(builder.build());
    }

    public CharSequence getFullyQualifiedName() {
        if (this.packageName != null && this.packageName.trim().length() > 0) {
            return this.packageName + "." + this.className;
        }
        return this.className;
    }

}
