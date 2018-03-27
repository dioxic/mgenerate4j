package com.dioxic.mgenerate.annotation.processor;

import com.dioxic.mgenerate.annotation.OperatorClass;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("com.dioxic.mgenerate.annotation.*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BuilderAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver() || annotations.size() == 0) {
            return false;
        }

        if (roundEnv.getRootElements() == null || roundEnv.getRootElements().isEmpty()) {
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "No sources to process");
            return false;
        }

        Util.elementUtils = this.processingEnv.getElementUtils();
        Util.typeUtils = this.processingEnv.getTypeUtils();
        Util.messager = this.processingEnv.getMessager();

        AnnotationHierarchyUtil hierarchyUtil = new AnnotationHierarchyUtil(this.processingEnv.getTypeUtils());

        List<TypeSpec> builderSpecs = new ArrayList<>();

        Set<TypeElement> triggeringAnnotations = hierarchyUtil
                .filterTriggeringAnnotations(annotations,
                        this.processingEnv.getElementUtils()
                                .getTypeElement(OperatorClass.class.getCanonicalName()));

        for (TypeElement annotation : triggeringAnnotations) {
            Set<? extends Element> elements = roundEnv
                    .getElementsAnnotatedWith(annotation).stream()
                    .filter(el -> el.getKind() == ElementKind.CLASS
                            && !el.getModifiers().contains(Modifier.ABSTRACT))
                    .collect(Collectors.toSet());

            for (Element element : elements) {
                try {
                    TypeElement typeElement = (TypeElement) element;
                    BuilderGenerator codeGen = new BuilderGenerator(typeElement);

                    JavaFileObject jfo = this.processingEnv.getFiler().createSourceFile(codeGen.getFullyQualifiedName());
                    try (Writer writer = jfo.openWriter()) {
                        TypeSpec builderSpec = codeGen.generate();

                        JavaFile javaFile = JavaFile.builder(codeGen.getPackageName(), builderSpec).build();
                        javaFile.writeTo(writer);

                        builderSpecs.add(builderSpec);
                    }
                } catch (Exception e) {
                    this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                }
            }
        }

        return true;
    }

}

