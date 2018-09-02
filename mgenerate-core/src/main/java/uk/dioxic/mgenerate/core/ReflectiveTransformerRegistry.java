package uk.dioxic.mgenerate.core;

import org.reflections.Reflections;
import uk.dioxic.mgenerate.common.Transformer;
import uk.dioxic.mgenerate.common.TransformerRegistry;
import uk.dioxic.mgenerate.common.annotation.ValueTransformer;

public class ReflectiveTransformerRegistry extends TransformerRegistry {

    private static ReflectiveTransformerRegistry instance = new ReflectiveTransformerRegistry();

    protected ReflectiveTransformerRegistry() {
        addTransformers("uk.dioxic.mgenerate.core.transformer");
    }

    public static ReflectiveTransformerRegistry getInstance() {
        return instance;
    }

    public void addTransformers(String packageName) {
        Reflections reflections = new Reflections(packageName);
        reflections.getTypesAnnotatedWith(ValueTransformer.class).stream()
                .filter(Transformer.class::isAssignableFrom)
                .map(this::instantiate)
                .map(Transformer.class::cast)
                .forEach(this::addTransformer);
    }

    private <T> T instantiate(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
