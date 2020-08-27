package uk.dioxic.mgenerate.core.operator;

import org.bson.Document;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.ResolvableBuilder;
import uk.dioxic.mgenerate.common.TransformerRegistry;
import uk.dioxic.mgenerate.common.annotation.OperatorBuilder;
import uk.dioxic.mgenerate.common.exception.TransformerException;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.HashMap;
import java.util.Map;

import static org.bson.assertions.Assertions.notNull;

public class OperatorFactory {

    private static final Logger logger = LoggerFactory.getLogger(OperatorFactory.class);

    private static final Map<String, Class<ResolvableBuilder<?>>> builderMap = new HashMap<>();

    static {
        addBuilders("uk.dioxic.mgenerate.core.operator");
    }

    @SuppressWarnings("unchecked")
    public static void addBuilders(String packageName) {
        Reflections reflections = new Reflections(packageName);
        reflections.getTypesAnnotatedWith(OperatorBuilder.class).stream()
                .filter(ResolvableBuilder.class::isAssignableFrom)
                .map(o -> (Class<ResolvableBuilder<?>>) o)
                .forEach(OperatorFactory::addBuilder);
    }

    public static void addBuilder(Class<ResolvableBuilder<?>> builderClass) {
        OperatorBuilder annotation = builderClass.getAnnotation(OperatorBuilder.class);

        notNull("operation builder class annoation", annotation);

        for (String key : annotation.value()) {
            addBuilder(key, builderClass);
        }
    }

    public static void addBuilder(String key, Class<ResolvableBuilder<?>> builderClass) {
        logger.trace("adding [{}] to operator registry", key.toLowerCase());
        builderMap.put(key.toLowerCase(), builderClass);
    }

    public static boolean canHandle(String operatorKey) {
        notNull("operatorKey", operatorKey);
        return isOperatorKey(operatorKey) && builderMap.containsKey(getOperatorKey(operatorKey));
    }

    private static boolean isOperatorKey(String key) {
        return key.startsWith("$");
    }

    private static String getOperatorKey(String key) {
        return key.substring(1).toLowerCase();
    }

    public static Resolvable<?> create(String operatorKey) {
        try {
            if (canHandle(operatorKey)) {
                Class<ResolvableBuilder<?>> builderClass = builderMap.get(getOperatorKey(operatorKey));
                return builderClass.getConstructor(TransformerRegistry.class).newInstance(ReflectiveTransformerRegistry.getInstance()).build();
            }
            return null;
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Resolvable<?> create(String operatorKey, Object object) {
        notNull("document", object);
        notNull("operatorKey", operatorKey);

        ResolvableBuilder<?> builder = createBuilder(operatorKey);

        if (object instanceof Document) {
            return builder.document((Document) object).build();
        }
        return builder.singleValue(object).build();
    }

    private static ResolvableBuilder<?> createBuilder(String operatorKey) {
        try {
            if (canHandle(operatorKey)) {
                Class<ResolvableBuilder<?>> builderClass = builderMap.get(getOperatorKey(operatorKey));
                return builderClass.getConstructor(TransformerRegistry.class).newInstance(ReflectiveTransformerRegistry.getInstance());
            }
            throw new TransformerException("no builder found for " + operatorKey);
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
