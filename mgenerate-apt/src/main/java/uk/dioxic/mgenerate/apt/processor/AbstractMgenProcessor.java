package uk.dioxic.mgenerate.apt.processor;

import uk.dioxic.mgenerate.apt.poet.Poet;
import uk.dioxic.mgenerate.apt.util.AnnotationHierarchyUtil;
import uk.dioxic.mgenerate.apt.util.ModelUtil;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

abstract class AbstractMgenProcessor extends AbstractProcessor {

    private Class<? extends Annotation> triggeringAnnotation;

    protected AbstractMgenProcessor(Class<? extends Annotation> triggeringAnnotation) {
        this.triggeringAnnotation = triggeringAnnotation;
    }

    protected abstract Poet getPoet(TypeElement typeElement);

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver() || annotations.size() == 0) {
            return false;
        }

        if (roundEnv.getRootElements() == null || roundEnv.getRootElements().isEmpty()) {
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "No sources to process");
            return false;
        }

        ModelUtil.elementUtils = this.processingEnv.getElementUtils();
        ModelUtil.typeUtils = this.processingEnv.getTypeUtils();
        ModelUtil.messager = this.processingEnv.getMessager();

        AnnotationHierarchyUtil hierarchyUtil = new AnnotationHierarchyUtil(this.processingEnv.getTypeUtils());

        Set<TypeElement> triggeringAnnotations = hierarchyUtil
                .filterTriggeringAnnotations(annotations,
                        this.processingEnv.getElementUtils()
                                .getTypeElement(triggeringAnnotation.getCanonicalName()));

        for (TypeElement annotation : triggeringAnnotations) {
            Set<TypeElement> typeElements = roundEnv
                    .getElementsAnnotatedWith(annotation).stream()
                    .filter(el -> el.getKind() == ElementKind.CLASS
                            && !el.getModifiers().contains(Modifier.ABSTRACT))
                    .map(o -> (TypeElement)o)
                    .collect(Collectors.toSet());

            for (TypeElement typeElement : typeElements) {
                try {
                    Poet poet = getPoet(typeElement);

                    JavaFileObject jfo = this.processingEnv.getFiler().createSourceFile(poet.getFullyQualifiedName());
                    try (Writer writer = jfo.openWriter()) {
                        poet.generate(writer);
                    }

                } catch (Exception e) {
                    this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                }
            }
        }

        return true;
    }
}
