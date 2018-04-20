package uk.dioxic.mgenerate;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.faker.resolvable.Resolvable;
import uk.dioxic.mgenerate.codec.DocumentCacheCodec;
import uk.dioxic.mgenerate.exception.DocumentNotMappedException;
import uk.dioxic.mgenerate.util.BsonUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DocumentValueCache {

    private static Logger logger = LoggerFactory.getLogger(DocumentValueCache.class);
    private static ThreadLocal<Map<Resolvable, Object>> resolverCache = ThreadLocal.withInitial(HashMap::new);
    private static Map<Document, Map<String, Object>> documentMap = new HashMap<>();
    private static ThreadLocal<Map<String, Object>> encodingContext = ThreadLocal.withInitial(HashMap::new);

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
        encodingContext.set(documentMap.get(document));
        resolverCache.get().clear();
    }

    /**
     * Maps the input {@link Document} to a one-dimension map suitable for dot-notation lookups.
     * @param document the document to map
     */
    public static void mapDocument(Document document) {
        documentMap.put(document, BsonUtil.flatMap(document));
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
    public static Object get(String coordinates) throws DocumentNotMappedException {
        return get(encodingContext.get(),coordinates);
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
    public static Object get(Document document, String coordinates) throws DocumentNotMappedException {
        return get(documentMap.get(document),coordinates);
    }

    private static Object get(Map<String, Object> map, String coordinates) throws DocumentNotMappedException {
        if (map == null) {
            throw new DocumentNotMappedException("coordinates [" + coordinates + "] could not be resolved");
        }

        Object object = map.get(coordinates);
        if (object instanceof Resolvable) {
            return get((Resolvable)object);
        }
        if (object == null) {
            logger.warn("coordinates [{}] could not be resolved - has the document been mapped?", coordinates);
        }
        return object;
    }

    public static Set<String> getKeys(Document document) {
        Map<String, Object> docMap = documentMap.get(document);
        if (docMap == null) {
            throw new DocumentNotMappedException();
        }
        return docMap.keySet();
    }

}
