package uk.dioxic.mgenerate.apt.processor;

import uk.dioxic.mgenerate.apt.poet.Poet;
import uk.dioxic.mgenerate.apt.poet.PojoGeneratorPoet;
import uk.dioxic.mgenerate.common.annotation.Pojo;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

@Deprecated
@SupportedAnnotationTypes("uk.dioxic.mgenerate.common.annotation.Pojo")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class PojoGeneratorProcessor extends AbstractMgenProcessor {

    public PojoGeneratorProcessor() {
        super(Pojo.class);
    }

    @Override
    protected Poet getPoet(TypeElement typeElement) {
        return new PojoGeneratorPoet(typeElement);
    }
}

