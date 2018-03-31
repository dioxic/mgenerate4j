package com.dioxic.mgenerate;

import com.dioxic.mgenerate.annotation.OperatorBuilderClass;
import com.dioxic.mgenerate.operator.Wrapper;
import org.bson.Document;
import org.bson.assertions.Assertions;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class WrapperFactory {

    private static final Logger logger = LoggerFactory.getLogger(WrapperFactory.class);

    private static final Map<String, Class<OperatorBuilder>> builderMap = new HashMap<>();

    static {
        addBuilders("com.dioxic.mgenerate.operator");
    }

    public static void addBuilders(String packageName) {
        Reflections reflections = new Reflections(packageName);
        reflections.getSubTypesOf(OperatorBuilder.class).stream()
                .map(o -> (Class<OperatorBuilder>) o)
                .forEach(WrapperFactory::addBuilder);
    }

    public static void addBuilder(Class<OperatorBuilder> builderClass) {
        OperatorBuilderClass annotation = builderClass.getAnnotation(OperatorBuilderClass.class);

        Assertions.notNull("operation builder class annoation", annotation);

        addBuilder(annotation.value(), builderClass);
    }

    public static void addBuilder(String key, Class<OperatorBuilder> builderClass) {
        builderMap.put(key, builderClass);
    }

    public static boolean contains(String operatorKey) {
        return builderMap.containsKey(operatorKey);
    }

    public static Resolvable create(String operatorKey) {
        try {
            return contains(operatorKey) ? builderMap.get(operatorKey).newInstance().build() : null;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Resolvable create(String operatorKey, Document doc) {
        Assertions.notNull("document", doc);

        if (contains(operatorKey)) {
            for (Map.Entry<String, Object> entry : doc.entrySet()) {
                entry.setValue(wrap(entry.getValue()));
            }

            try {
                return builderMap.get(operatorKey).newInstance().document(doc).build();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    public static <T> Resolvable<T> wrap(T object) {
        if (object instanceof Resolvable) {
            return (Resolvable) object;
        }
        return new Wrapper<>(object);
    }
}
