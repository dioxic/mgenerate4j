package uk.dioxic.mgenerate.apt.processor;

import uk.dioxic.mgenerate.annotation.Operator;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.Writer;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("uk.dioxic.mgenerate.annotation.*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
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

        Util.elementUtils = this.processingEnv.getElementUtils();
        Util.typeUtils = this.processingEnv.getTypeUtils();
        Util.messager = this.processingEnv.getMessager();

        AnnotationHierarchyUtil hierarchyUtil = new AnnotationHierarchyUtil(this.processingEnv.getTypeUtils());

        Set<TypeElement> triggeringAnnotations = hierarchyUtil
                .filterTriggeringAnnotations(annotations,
                        this.processingEnv.getElementUtils()
                                .getTypeElement(Operator.class.getCanonicalName()));

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
                        builderGen.generate(writer);
                    }

                } catch (Exception e) {
                    this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                }
            }
        }

        return true;
    }

}

