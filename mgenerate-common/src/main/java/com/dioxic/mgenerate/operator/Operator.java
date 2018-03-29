package com.dioxic.mgenerate.operator;

import java.util.Arrays;
import java.util.List;

import static org.bson.assertions.Assertions.notNull;

public interface Operator<T> {

    T resolve();

    static <T> T resolve(final Operator operator, final Class<T> clazz) {
        notNull("clazz", clazz);
        return clazz.cast(operator);
    }

    static Integer resolveInt(Operator operator) {
        return (Integer) operator.resolve();
    }

    static Long resolveLong(Operator operator) {
        return (Long) operator.resolve();
    }

    static String resolveString(Operator operator) {
        return (String) operator.resolve();
    }

    static Integer[] resolveIntArray(Operator operator) {
        Object obj = operator.resolve();

        if (obj instanceof Object[]) {
            return Arrays.stream((Object[]) obj).toArray(Integer[]::new);
        }
        if (obj instanceof List<?>) {
            return ((List<?>) obj).toArray(new Integer[0]);
        }
        throw new IllegalArgumentException("expected array");
    }

    static Object[] resolveArray(Operator operator) {
        Object obj = operator.resolve();

        if (obj instanceof Object[]) {
            return (Object[]) obj;
        }
        if (obj instanceof List<?>) {
            return ((List<?>) obj).toArray();
        }
        return new Object[]{obj};
    }

    static List<?> resolveList(Operator operator) {
        return resolveList(operator, List.class);
    }

    static <T> List<T> resolveList(Operator operator, Class<T> clazz) {
        Object obj = operator.resolve();

        if (obj instanceof Object[]) {
            return Arrays.asList((T[]) obj);
        }
        if (obj instanceof List<?>) {
            return  (List<T>) obj;
        }
        return Arrays.asList((T) obj);
    }

//    static Operator create(String operator) {
//        return create(operator, null);
//    }
//
//    static Operator create(String operator, Document doc) {
//        if (doc != null) {
//            for (Entry<String, Object> entry : doc.entrySet()) {
//                entry.setValue(wrap(entry.getValue()));
//            }
//        }
//
//        if (operator.startsWith("$")) {
//            operator = operator.substring(1);
//        }
//
//        switch (operator) {
//            case "array":
//                return new Array(doc);
//            case "choose":
//                return new Choose(doc);
//            case "number":
//                return new com.dioxic.mgenerate.operator.Number(doc);
//            case "objectid":
//                return new ObjectId();
//            case "street":
//                return new Street();
//            case "city":
//                return new City();
//            case "postal":
//                return new Postal();
//            case "email":
//                return new Email(doc);
//            case "date":
//                return new Date(doc);
//            default:
//                return null;
//        }
//    }

}
