package uk.dioxic.mgenerate.core.util;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.mgenerate.common.Resolvable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentUtil {

    private static final Logger logger = LoggerFactory.getLogger(DocumentUtil.class);

    public static Object coordinateLookup(String coordinates, Object input) {
        return coordinateLookup(coordinates, input, null, false);
    }

    @SuppressWarnings("unchecked")
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

        if (input instanceof Iterable) {
            Iterable<?> list = (Iterable) input;
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
     * @param hydrate hydrate {@link uk.dioxic.mgenerate.common.Resolvable} objects
     * @param o input object
     */
    @SuppressWarnings("unchecked")
    public static Object flatMap(Map<String, Object> flatMap, String key, boolean hydrate, Object o) {
        if (flatMap.containsKey(key)) {
            return flatMap.get(key);
        }

        if (hydrate && o instanceof Resolvable) {
            Resolvable r = (Resolvable) o;
            o = r.resolve();
        }

        if (o instanceof Map) {
            Document doc = new Document();
            ((Map<String, Object>) o).forEach((k, v) -> {
                doc.put(k, flatMap(flatMap, key + "." + k, hydrate, v));
            });
            flatMap.put(key, doc);
            return doc;
        } else if (o instanceof Iterable) {
            List<Object> l = new ArrayList<>((List<?>)o);
            for (int i=0; i< l.size(); i++) {
                l.set(i, flatMap(flatMap, key + "." + i, hydrate, l.get(i)));
            }
            flatMap.put(key, l);
            return l;
        }
        logger.trace("adding {} to flat map", key);
        flatMap.put(key, o);

        return o;
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
            ((Map<String, Object>) o).forEach((k, v) -> flatMap(flatMap, k, false, v));
        } else if (o instanceof Iterable) {
            int counter = 0;
            for (Object item : (Iterable) o) {
                flatMap(flatMap, Integer.toString(counter++), false, item);
            }
        }
    }

    @Deprecated
    private static String getNewKey(String key, Map.Entry<String, Object> entry) {
        String newKey = key == null ? entry.getKey() : key + "." + entry.getKey();
        return newKey.replace('-', '_');
    }

}
