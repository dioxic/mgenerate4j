package com.dioxic.mgenerate;

import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.operator.*;
import org.bson.Document;
import org.bson.assertions.Assertions;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.constructor.ConstructorException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class OperatorFactory {

    private static final Logger logger = LoggerFactory.getLogger(OperatorFactory.class);

//    private static final List<OperatorProvider> providers = new ArrayList<>();

//    private static final Map<String, Set<Constructor>> constructorMap = new HashMap<>();

    private static final Map<String, OperatorBuilder> builderMap = new HashMap<>();


    static {
//        addProvider(DefaultOperatorProvider.instance());

        Reflections reflections = new Reflections("com.dioxic.mgenerate.operator");

//        reflections.getTypesAnnotatedWith(OperatorClass.class).stream()
//                .filter(Operator.class::isAssignableFrom)
//                .forEach(clazz -> {
//                    OperatorClass annotation = clazz.getAnnotation(OperatorClass.class);
//                    constructorMap.put(getOperatorKey(clazz, annotation), ReflectionUtils.getConstructors(clazz));
//                });

        reflections.getSubTypesOf(OperatorBuilder.class).stream()
                .map(o -> (Class<OperatorBuilder>) o)
                .map(OperatorFactory::instantiate)
                .forEach(OperatorFactory::addBuilder);

    }

    private static OperatorBuilder instantiate(Class<OperatorBuilder> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getOperatorKey(Class<?> clazz, OperatorClass annotation) {
        if (annotation.value().isEmpty()) {
            char[] key = clazz.getSimpleName().toCharArray();
            key[0] = Character.toLowerCase(key[0]);
            return String.valueOf(key);
        }

        return annotation.value();
    }

    public static void addBuilder(OperatorBuilder builder) {
        builderMap.put(builder.getKey(), builder);
    }

//    public static void addProvider(OperatorProvider provider) {
//        providers.add(provider);
//    }

    public static boolean contains(String operatorKey) {
        return builderMap.containsKey(operatorKey);
    }

    public static Operator create(String operatorKey) {
//        if (contains(operatorKey)) {
//            for (Constructor c : constructorMap.get(operatorKey)) {
//                if (c.getParameterCount() == 0) {
//                    try {
//                        return (Operator) c.newInstance();
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//        }

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

//        if (contains(operatorKey)) {
//            for (Constructor c : constructorMap.get(operatorKey)) {
//                if (c.getParameterCount() == 1 && c.getParameterTypes()[0] == Document.class) {
//                    try {
//                        return (Operator) c.newInstance(doc);
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//            logger.warn("no document constructor found for ${} operator", operatorKey);
//        }

//        for (OperatorProvider provider : providers) {
//            if (provider.provides(operatorKey)) {
//                return provider.get(operatorKey)
//                        .document(doc)
//                        .build();
//            }
//        }

        return null;
    }

    public static Operator wrap(Object object) {
        if (object instanceof Operator) {
            return (Operator) object;
        }
        return new Wrapper(object);
    }
}
