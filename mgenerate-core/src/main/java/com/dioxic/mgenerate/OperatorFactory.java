package com.dioxic.mgenerate;

import com.dioxic.mgenerate.operator.Operator;
import com.dioxic.mgenerate.operator.OperatorBuilder;
import com.dioxic.mgenerate.operator.Wrapper;
import org.bson.Document;
import org.bson.assertions.Assertions;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class OperatorFactory {

    private static final Logger logger = LoggerFactory.getLogger(OperatorFactory.class);

    private static final Map<String, OperatorBuilder> builderMap = new HashMap<>();

    static {
        addBuilders("com.dioxic.mgenerate.operator");
    }

    private static OperatorBuilder instantiate(Class<OperatorBuilder> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addBuilders(String packageName) {
        Reflections reflections = new Reflections(packageName);

        reflections.getSubTypesOf(OperatorBuilder.class).stream()
                .map(o -> (Class<OperatorBuilder>) o)
                .map(OperatorFactory::instantiate)
                .forEach(OperatorFactory::addBuilder);
    }

    public static void addBuilder(OperatorBuilder builder) {
        builderMap.put(builder.getKey(), builder);
    }

    public static boolean contains(String operatorKey) {
        return builderMap.containsKey(operatorKey);
    }

    public static Operator create(String operatorKey) {
        return contains(operatorKey) ? builderMap.get(operatorKey).build() : null;
    }

    public static Operator create(String operatorKey, Document doc) {
        Assertions.notNull("document", doc);

        if (contains(operatorKey)) {
            for (Map.Entry<String, Object> entry : doc.entrySet()) {
                entry.setValue(wrap(entry.getValue()));
            }

            return builderMap.get(operatorKey).document(doc).build();
        }

        return null;
    }

    public static <T> Operator<T> wrap(T object) {
        if (object instanceof Operator) {
            return (Operator) object;
        }
        return new Wrapper(object);
    }
}
