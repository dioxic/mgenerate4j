package uk.dioxic.mgenerate.core;

import org.bson.Document;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.ResolvableBuilder;
import uk.dioxic.mgenerate.common.TransformerRegistry;
import uk.dioxic.mgenerate.common.annotation.OperatorBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.bson.assertions.Assertions.notNull;

public class OperatorFactory {

    private static final Logger logger = LoggerFactory.getLogger(OperatorFactory.class);

    private static final Map<String, Class<ResolvableBuilder>> builderMap = new HashMap<>();

    static {
        addBuilders("uk.dioxic.mgenerate.core.operator");
    }

    @SuppressWarnings("unchecked")
    public static void addBuilders(String packageName) {
        Reflections reflections = new Reflections(packageName);
        reflections.getTypesAnnotatedWith(OperatorBuilder.class).stream()
                .filter(ResolvableBuilder.class::isAssignableFrom)
                .map(o -> (Class<ResolvableBuilder>) o)
                .forEach(OperatorFactory::addBuilder);
    }

    public static void addBuilder(Class<ResolvableBuilder> builderClass) {
        OperatorBuilder annotation = builderClass.getAnnotation(OperatorBuilder.class);

        notNull("operation builder class annoation", annotation);

        for (String key : annotation.value()) {
            addBuilder(key, builderClass);
        }
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
            if (canHandle(operatorKey)) {
                Class<ResolvableBuilder> builderClass = builderMap.get(getOperatorKey(operatorKey));
                return builderClass.getConstructor(TransformerRegistry.class).newInstance(ReflectiveTransformerRegistry.getInstance()).build();
            }
            return null;
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Resolvable create(String operatorKey, Document doc) {
        notNull("document", doc);
        notNull("operatorKey", operatorKey);

        return Optional.ofNullable(createBuilder(operatorKey))
                .map(b -> b.document(doc).build())
                .orElse(null);
    }

    private static ResolvableBuilder createBuilder(String operatorKey) {
        try {
            if (canHandle(operatorKey)) {
                Class<ResolvableBuilder> builderClass = builderMap.get(getOperatorKey(operatorKey));
                return builderClass.getConstructor(TransformerRegistry.class).newInstance(ReflectiveTransformerRegistry.getInstance());
            }
            return null;
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
