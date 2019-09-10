package uk.dioxic.mgenerate.core.util;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(BsonUtil.class);

    public static Object resolveCoordinate(String coordinates, Object input) {
        return resolveCoordinate(coordinates, input, null, false);
    }

    public static Object recursiveResolve(Object o) {
        logger.trace("recursively resolving {}", o.toString());
        if (o instanceof Resolvable) {
            Resolvable r = (Resolvable) o;
            Object res = recursiveResolve(r.resolve());
            return res;
        }
        else if (o instanceof Document) {
            Document doc = new Document();
            ((Document) o).forEach((k, v) -> {
                v = recursiveResolve(v);
                doc.put(k, v);
            });
            return doc;
        }
        else if (o instanceof List) {
            List<Object> l = new ArrayList<>((List<?>)o);

            for (int i=0; i< l.size(); i++) {
                l.set(i, recursiveResolve(l.get(i)));
            }

            return l;
        }
        return o;
    }

    private static Object resolveCoordinate(String coordinates, Object input, Object result, boolean hasList) {
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
            list.forEach(item -> resolveCoordinate(coordinates, item, res, true));
            return resList;
        } else if (input instanceof Document) {
            Document doc = (Document) input;
            return resolveCoordinate(childCoordinates, doc.get(root), result, hasList);
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

        map(flatMap, null, document);

        return flatMap;
    }

    @SuppressWarnings("unchecked")
    public static void map(Map<String, Object> flatMap, String key, Object o) {
        if (o instanceof Map) {
            ((Map) o).entrySet().forEach(m -> map(flatMap, key, m));
            if (key != null) {
                flatMap.put(key, o);
            }
        } else if (o instanceof Map.Entry) {
            Map.Entry entry = ((Map.Entry) o);
            map(flatMap, getNewKey(key, entry), entry.getValue());
        } else if (o instanceof List) {
            int counter = 0;
            for (Object item : (List) o) {
                map(flatMap, key + "." + counter++, item);
            }
            flatMap.put(key, o);
        } else {
            if (key != null) {
                flatMap.put(key, o);
            }
        }
    }

    private static String getNewKey(String key, Map.Entry<String, Object> entry) {
        String newKey = key == null ? entry.getKey() : key + "." + entry.getKey();
        return newKey.replace('-', '_');
    }

}
