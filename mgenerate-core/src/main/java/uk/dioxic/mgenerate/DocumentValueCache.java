package uk.dioxic.mgenerate;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.faker.resolvable.Resolvable;
import uk.dioxic.mgenerate.codec.DocumentCacheCodec;
import uk.dioxic.mgenerate.exception.DocumentNotMappedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DocumentValueCache {

    private static Logger logger = LoggerFactory.getLogger(DocumentValueCache.class);
    private static ThreadLocal<Map<Resolvable, Object>> resolverCache = ThreadLocal.withInitial(() -> new HashMap<>());
    private static Map<Document, Map<String, Object>> documentMap = new HashMap<>();
    private static ThreadLocal<Map<String, Object>> encodingContext = ThreadLocal.withInitial(() -> new HashMap<>());

    /**
     * Returns the cached value of the input {@link Resolvable} or, if
     * a cached value does not exist, the value is resolved, cached and returned.
     * @param resolvable
     * @return the resolved/cached value
     */
    public static Object get(Resolvable resolvable) {
        Object value = resolverCache.get().get(resolvable);
        if (value == null) {
            value = resolvable.resolve();
            resolverCache.get().put(resolvable, value);
        }
        return value;
    }

    /**
     * Clears all stored data for the current thread.
     */
    public static void clearThread() {
        resolverCache.get().clear();
        encodingContext.get().clear();
    }

    /**
     * Purges the document lookup mapping.
     * @param document the document to purge
     */
    public static void purgeDocument(Document document) {
        documentMap.remove(document);
    }

    /**
     * Sets the thread encoding context to the supplied {@link Document}.
     * <p>
     * This should be set prior to encoding the {@link Document} and will be set
     * automatically if using the {@link DocumentCacheCodec}
     * @param document the document being encoding
     */
    public static void setEncodingContext(Document document) {
        encodingContext.set(getFlatMap(document));
        resolverCache.get().clear();
    }

    /**
     * Maps the input {@link Document} to a one-dimension map suitable for dot-notation lookups.
     * @param document the document to map
     */
    public static void mapDocument(Document document) {
        Map<String, Object> flatMap = new HashMap<>();

        map(flatMap, null, document);

        documentMap.put(document, flatMap);
    }

    /**
     * Gets the value of the dot-notation coordinates for the document
     * currently being encoded by the calling thread.
     * <p>
     * If the coordinates refer to a {@link Resolvable} then a resolved value
     * will be returned. If a cache entry for the {@link Resolvable} exists, this will
     * be returned, otherwise, the value will be resolved, cached, and then returned.
     * <p>
     * Thus, multiple calls within the same encoding context for the same coordinate will always
     * result in the same returned value.
     * @param coordinates dot-notation coordinates
     * @return resolved/cached valued
     */
    public static Object get(String coordinates) {
        return get(encodingContext.get().get(coordinates));
    }

    /**
     * Gets the value of the dot-notation coordinates for the supplied document.
     * <p>
     * If the coordinates refer to a {@link Resolvable} then a resolved value
     * will be returned. If a cache entry for the {@link Resolvable} exists, this will
     * be returned, otherwise, the value will be resolved, cached, and then returned.
     * <p>
     * Thus, multiple calls for the same document/coordinate will always
     * result in the same returned value.
     * <p>
     * If the document has not been mapped, the
     * @param coordinates dot-notation coordinates
     * @return resolved/cached valued
     */
    public static Object get(Document document, String coordinates) {
        return get(getFlatMap(document).get(coordinates));
    }

    private static Object get(Object object) {
        if (object instanceof Resolvable) {
            return get((Resolvable)object);
        }
        if (object == null) {
            logger.warn("coordinates [{}] could not be resolved - has the document been mapped?");
        }
        return object;
    }

    public static Set<String> getKeys(Document document) {
        return getFlatMap(document).keySet();
    }

    private static Map<String, Object> getFlatMap(Document document) {
        Map<String, Object> flatMap = documentMap.get(document);
        if (flatMap == null) {
            throw new DocumentNotMappedException();
        }
        return flatMap;
    }

    private static void map(Map<String, Object> flatMap, String key, Object o) {
        if (o instanceof Map) {
            Map<String, Object> localMap = ((Map) o);

            localMap.entrySet().forEach(m -> map(flatMap, key, m));
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
