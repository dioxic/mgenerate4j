package com.dioxic.mgenerate.annotation.processor;

import com.dioxic.mgenerate.annotation.OperatorClass;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.*;
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
@SupportedOptions({"operatorProviderClassName", "operatorProviderPackage"})
public class OperatorAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver() || annotations.size() == 0) {
            return false;
        }

        if (roundEnv.getRootElements() == null || roundEnv.getRootElements().isEmpty()) {
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "No sources to process");
            return false;
        }

        String providerPackageName = this.processingEnv.getOptions().get("operatorProviderPackage");
        String providerClassName = this.processingEnv.getOptions().get("operatorProviderClassName");
        if (providerClassName == null) {
            providerClassName = "DefaultOperatorProvider";
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
            Set<TypeElement> typeElements = roundEnv
                    .getElementsAnnotatedWith(annotation).stream()
                    .filter(el -> el.getKind() == ElementKind.CLASS
                            && !el.getModifiers().contains(Modifier.ABSTRACT))
                    .map(o -> (TypeElement)o)
                    .collect(Collectors.toSet());

            for (TypeElement typeElement : typeElements) {
                try {
                    BuilderGenerator builderGen = new BuilderGenerator(typeElement);

                    JavaFileObject jfo = this.processingEnv.getFiler().createSourceFile(builderGen.getFullyQualifiedName());
                    try (Writer writer = jfo.openWriter()) {
                        TypeSpec builderSpec = builderGen.generate(writer);
                        builderSpecs.add(builderSpec);
                    }

                    if (providerPackageName == null){
                        providerPackageName = builderGen.getPackageName();
                    }
                } catch (Exception e) {
                    this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                }
            }
        }

        try {
            ProviderGenerator providerGen = new ProviderGenerator(providerPackageName, providerClassName, builderSpecs);
            JavaFileObject jfo = this.processingEnv.getFiler().createSourceFile(providerGen.getFullyQualifiedName());
            try (Writer writer = jfo.openWriter()) {
                providerGen.generate(writer);
            }
        } catch (Exception e) {
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }

        return true;
    }

}

