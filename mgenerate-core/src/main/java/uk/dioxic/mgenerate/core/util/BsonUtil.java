package uk.dioxic.mgenerate.core.util;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(BsonUtil.class);

    public static Object coordinateLookup(String coordinates, Object input) {
        return coordinateLookup(coordinates, input, null, false);
    }

    private static Object coordinateLookup(String coordinates, Object input, Object result, boolean hasList) {
        String root;
        String childCoordinates;

        if (coordinates == null) {
            root = null;
            childCoordinates = null;
        } else {
            // a leaf node
            int first = coordinates.indexOf('.');
            if (first == -1) {
                root = coordinates;
                childCoordinates = null;
            } else {
                root = coordinates.substring(0, first);
                childCoordinates = coordinates.substring(first + 1);
            }
        }

        if (input instanceof List) {
            List<?> list = (List) input;
            List<?> resList = (List) result;
            if (resList == null) {
                resList = new ArrayList<>();
            }
            final Object res = resList;
            list.forEach(item -> coordinateLookup(coordinates, item, res, true));
            return resList;
        } else if (input instanceof Document) {
            Document doc = (Document) input;
            return coordinateLookup(childCoordinates, doc.get(root), result, hasList);
        } else if (root == null) {
            if (hasList) {
                List<Object> resList = (List) result;
                if (input != null) {
                    resList.add(input);
                }
            } else {
                return input;
            }
        }

        return null;
    }

    /**
     * Maps the input {@link Document} to a one-dimension map suitable for dot-notation lookups.
     * @param document the document to map
     */
    public static Map<String, Object> flatMap(Document document) {
        Map<String, Object> flatMap = new HashMap<>();

        flatMap(flatMap, document);

        return flatMap;
    }

    /**
     * Maps the input {@link Object} to a one-dimension map suitable for dot-notation lookups.
     * @param flatMap target map
     * @param key dot-notation key for the input object
     * @param o input object
     */
    @SuppressWarnings("unchecked")
    public static void flatMap(Map<String, Object> flatMap, String key, Object o) {
        if (o instanceof Map) {
            ((Map) o).forEach((k, v) -> flatMap(flatMap, key + "." + k, v));
        } else if (o instanceof List) {
            int counter = 0;
            for (Object item : (List) o) {
                flatMap(flatMap, key + "." + counter++, item);
            }
        }
        flatMap.put(key, o);
    }

    /**
     * Maps the input {@link Object} to a one-dimension map suitable for dot-notation lookups.
     * <br>
     * @param flatMap target map
     * @param o input object
     */
    @SuppressWarnings("unchecked")
    public static void flatMap(Map<String, Object> flatMap, Object o) {
        if (o instanceof Map) {
            ((Map<String, Object>) o).forEach((k, v) -> flatMap(flatMap, k, v));
        } else if (o instanceof List) {
            int counter = 0;
            for (Object item : (List) o) {
                flatMap(flatMap, Integer.toString(counter++), item);
            }
        }
    }

    @Deprecated
    private static String getNewKey(String key, Map.Entry<String, Object> entry) {
        String newKey = key == null ? entry.getKey() : key + "." + entry.getKey();
        return newKey.replace('-', '_');
    }

}
