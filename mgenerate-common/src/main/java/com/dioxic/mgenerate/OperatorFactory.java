package com.dioxic.mgenerate;

import com.dioxic.mgenerate.annotation.ValueTransformer;
import com.dioxic.mgenerate.transformer.DateTransformer;
import com.dioxic.mgenerate.annotation.OperatorBuilderClass;
import com.dioxic.mgenerate.operator.Wrapper;
import org.bson.Document;
import org.bson.Transformer;
import org.bson.assertions.Assertions;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class OperatorFactory {

    private static final Logger logger = LoggerFactory.getLogger(OperatorFactory.class);

    private static final Map<String, Class<OperatorBuilder>> builderMap = new HashMap<>();
    private static final Map<Class, Transformer> transformerMap = new HashMap<>();

    static {
        addBuilders("com.dioxic.mgenerate.operator");
        //transformerMap.put(LocalDateTime.class, new DateTransformer());
        addTransformers("com.dioxic.mgenerate.transformer");
        addTransformers("com.dioxic.mgenerate.operator");
    }

    public static void addBuilders(String packageName) {
        Reflections reflections = new Reflections(packageName);
        reflections.getTypesAnnotatedWith(OperatorBuilderClass.class).stream()
                .filter(OperatorBuilder.class::isAssignableFrom)
                .map(o -> (Class<OperatorBuilder>) o)
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
        transformerMap.put(annotation.value(), transformer);
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
            return instantiate(builderMap.get(operatorKey)).document(doc).build();
        }

        return null;
    }

    public static <T> Resolvable<T> wrap(T object) {
        if (object != null) {
            if (object instanceof Resolvable) {
                return (Resolvable<T>) object;
            }
            return new Wrapper<>(object);
        }
        return null;
    }

    public static Resolvable wrap(Object object, Class desiredType) {
        if (object != null) {
            if (transformerMap.containsKey(desiredType)) {
                return new Wrapper<>(object, transformerMap.get(desiredType));
            }
            return wrap(object);
        }
        return null;
    }
}
