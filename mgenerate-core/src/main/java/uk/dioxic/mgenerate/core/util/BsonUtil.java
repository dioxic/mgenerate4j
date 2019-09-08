package uk.dioxic.mgenerate.core.util;

import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BsonUtil {

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
    private static void map(Map<String, Object> flatMap, String key, Object o) {
        if (o instanceof Map) {
            ((Map) o).entrySet().forEach(m -> map(flatMap, key, m));
            if (key != null) {
                flatMap.put(key, o);
            }
        }
        else if (o instanceof Map.Entry) {
            Map.Entry entry = ((Map.Entry) o);
            map(flatMap, getNewKey(key, entry), entry.getValue());
        }
        else if (o instanceof List) {
            int counter = 0;
            for (Object item : (List)o) {
                map(flatMap, key + "." + counter++, item);
            }
            flatMap.put(key, o);
        }
        else {
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
