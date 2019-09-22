package uk.dioxic.mgenerate.apt.processor;

import uk.dioxic.mgenerate.apt.poet.OperatorBuilderPoet;
import uk.dioxic.mgenerate.apt.util.AnnotationHierarchyUtil;
import uk.dioxic.mgenerate.apt.util.ModelUtil;
import uk.dioxic.mgenerate.common.annotation.Operator;

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
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("uk.dioxic.mgenerate.common.annotation.Operator")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class OperatorBuilderProcessor extends AbstractProcessor {

    public OperatorBuilderProcessor() {}

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
                                .getTypeElement(Operator.class.getCanonicalName()));

        for (TypeElement annotation : triggeringAnnotations) {
            Set<TypeElement> typeElements = roundEnv
                    .getElementsAnnotatedWith(annotation).stream()
                    .filter(el -> el.getKind() == ElementKind.CLASS
                            && !el.getModifiers().contains(Modifier.ABSTRACT))
                    .map(TypeElement.class::cast)
                    .collect(Collectors.toSet());

            for (TypeElement typeElement : typeElements) {
                try {
                    OperatorBuilderPoet poet = new OperatorBuilderPoet(typeElement);
                    this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "found @Operator at " + typeElement);
                    JavaFileObject jfo = this.processingEnv.getFiler().createSourceFile(poet.getFullyQualifiedName());
                    try (Writer writer = jfo.openWriter()) {
                        poet.generate(writer);
                    }
                } catch (Exception e) {
                    String stacktrace = Arrays.stream(e.getStackTrace())
                            .map(StackTraceElement::toString)
                            .collect(Collectors.joining("\n"));
                    this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage() + "\n" + stacktrace);
                }
            }
        }

        return true;
    }

}

