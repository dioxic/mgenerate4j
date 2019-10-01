package uk.dioxic.mgenerate.core.transformer;

import org.reflections.Reflections;
import uk.dioxic.mgenerate.common.Transformer;
import uk.dioxic.mgenerate.common.TransformerRegistry;
import uk.dioxic.mgenerate.common.annotation.ValueTransformer;

public class ReflectiveTransformerRegistry extends TransformerRegistry {

    private static ReflectiveTransformerRegistry instance = new ReflectiveTransformerRegistry();

    public static ReflectiveTransformerRegistry getInstance() {
        return instance;
    }

    private ReflectiveTransformerRegistry() {
        addTransformers(ReflectiveTransformerRegistry.class.getPackage().getName());
    }

    /**
     * Add {@link Transformer} classes to the registry.
     * <p>Classes must be annotated with {@link ValueTransformer}.</p>
     * @param packageName fully qualified package name
     */
    public void addTransformers(String packageName) {
        Reflections reflections = new Reflections(packageName);

        reflections.getTypesAnnotatedWith(ValueTransformer.class).stream()
                .filter(Transformer.class::isAssignableFrom)
                .map(this::instantiate)
                .forEach(this::addTransformer);
    }

    private Transformer instantiate(Class<?> clazz) {
        try {
            return (Transformer)clazz.getConstructor().newInstance();
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
