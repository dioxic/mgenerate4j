package uk.dioxic.mgenerate;

import org.bson.Document;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.faker.resolvable.Resolvable;
import uk.dioxic.mgenerate.annotation.OperatorBuilder;
import uk.dioxic.mgenerate.annotation.ValueTransformer;
import uk.dioxic.mgenerate.operator.Wrapper;
import uk.dioxic.mgenerate.transformer.Transformer;

import java.util.HashMap;
import java.util.Map;

import static org.bson.assertions.Assertions.notNull;

public class OperatorFactory {

    private static final Logger logger = LoggerFactory.getLogger(OperatorFactory.class);

    private static final Map<String, Class<ResolvableBuilder>> builderMap = new HashMap<>();
    private static final Map<Class, Transformer> transformerMap = new HashMap<>();

    static {
        addBuilders("uk.dioxic.mgenerate.operator");
        addTransformers("uk.dioxic.mgenerate.transformer");
    }

    @SuppressWarnings("unchecked")
    public static void addBuilders(String packageName) {
        Reflections reflections = new Reflections(packageName);
        reflections.getTypesAnnotatedWith(OperatorBuilder.class).stream()
                .filter(ResolvableBuilder.class::isAssignableFrom)
                .map(o -> (Class<ResolvableBuilder>) o)
                .forEach(OperatorFactory::addBuilder);
    }

    public static void addTransformers(String packageName) {
        Reflections reflections = new Reflections(packageName);
        reflections.getTypesAnnotatedWith(ValueTransformer.class).stream()
                .filter(Transformer.class::isAssignableFrom)
                .map(OperatorFactory::instantiate)
                .map(Transformer.class::cast)
                .forEach(OperatorFactory::addTransformer);
    }

    private static <T> T instantiate(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addTransformer(Transformer transformer) {
        ValueTransformer annotation = transformer.getClass().getAnnotation(ValueTransformer.class);
        logger.debug("adding [{}] to transformer registry", annotation.value());
        transformerMap.put(annotation.value(), transformer);
    }

    public static void addBuilder(Class<ResolvableBuilder> builderClass) {
        OperatorBuilder annotation = builderClass.getAnnotation(OperatorBuilder.class);

        notNull("operation builder class annoation", annotation);

        addBuilder(annotation.value(), builderClass);
    }

    public static void addBuilder(String key, Class<ResolvableBuilder> builderClass) {
        logger.debug("adding [{}] to operator registry", key);
        builderMap.put(key, builderClass);
    }

    public static boolean canHandle(String operatorKey) {
        notNull("operatorKey", operatorKey);
        return isOperatorKey(operatorKey) && builderMap.containsKey(getOperatorKey(operatorKey));
    }

    private static boolean isOperatorKey(String key) {
        return key.startsWith("$");
    }

    private static String getOperatorKey(String key) {
        return key.substring(1);
    }

    public static Resolvable create(String operatorKey) {
        try {
            return canHandle(operatorKey) ? builderMap.get(getOperatorKey(operatorKey)).newInstance().build() : null;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Resolvable create(String operatorKey, Document doc) {
        notNull("document", doc);
        notNull("operatorKey", operatorKey);

        if (canHandle(operatorKey)) {
            return instantiate(builderMap.get(getOperatorKey(operatorKey))).document(doc).build();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> Resolvable<T> wrap(T object) {
        if (object != null) {
            if (object instanceof Resolvable) {
                return (Resolvable<T>) object;
            }
            return new Wrapper<>(object);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> Resolvable<T> wrap(Object object, Class<T> desiredType) {
        if (object != null) {
            if (transformerMap.containsKey(desiredType)) {
                return new Wrapper<>(object, transformerMap.get(desiredType));
            }
            if (desiredType.isAssignableFrom(object.getClass())) {
                return wrap((T)object);
            }
            if (object instanceof Resolvable) {
                return (Resolvable<T>)object;
            }
            throw new IllegalStateException("cannot wrap " + object.getClass().getSimpleName() + " to the desired type of " + desiredType.getSimpleName());
        }
        return null;
    }
}
