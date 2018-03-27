package com.dioxic.mgenerate.annotation.processor;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.TreeSet;

public class HelloWorldGenerator {

    private final TypeElement typeElement;

    private final String packageName;

    private final String className;

    private final ClassName thisType;

    private String providerClassName;

    private boolean ignoreUnknown;

    private final static TypeMirror typeMirrorOfObject = Util.elementUtils
            .getTypeElement(Object.class.getCanonicalName()).asType();

    public HelloWorldGenerator(TypeElement typeElement) {
        this.typeElement = typeElement;
        this.packageName = Util.elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        this.className = typeElement.getSimpleName() + "HelloWorld";
        this.thisType = ClassName.get(typeElement);
        if (this.packageName != null && !this.packageName.trim().isEmpty()) {
            this.providerClassName = this.packageName + ".world";
        }
        else {
            this.providerClassName = "hello.world";
        }

        this.ignoreUnknown = true;
    }

    public String getClassName() {
        return this.className;
    }

    public String getProviderClassName() {
        return this.providerClassName;
    }

    public CharSequence getFullyQualifiedName() {
        if (this.packageName != null && this.packageName.trim().length() > 0) {
            return this.packageName + "." + this.className;
        }
        return this.className;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void generate(Appendable appendable) throws IOException {

        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();

        TypeSpec builder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(Hello.class), this.thisType))
                .addMethod(main)
                .build();

//        List<FieldModel> fieldInfos = collectFields();
//        addEncodeMethod(classBuilder, fieldInfos);
//        addDecodeMethod(classBuilder, fieldInfos);
//        addGetEncoderClassMethod(classBuilder);
//
//        addInstanceFields(classBuilder);
//        addConstructor(classBuilder);

        JavaFile javaFile = JavaFile.builder(this.packageName, builder).build();
        javaFile.writeTo(appendable);
    }

}
