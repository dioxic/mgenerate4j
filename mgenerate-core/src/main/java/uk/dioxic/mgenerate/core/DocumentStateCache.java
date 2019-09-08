package uk.dioxic.mgenerate.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.State;
import uk.dioxic.mgenerate.common.exception.DocumentNotMappedException;

import java.util.HashMap;
import java.util.Map;

public class DocumentStateCache {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static DocumentStateCache instance;
    private ThreadLocal<DocumentState> localState = ThreadLocal.withInitial(DocumentState::new);

    public static DocumentStateCache getInstance() {
        if (instance == null) {
            instance = new DocumentStateCache();
        }
        return instance;
    }

    private DocumentStateCache() {
    }

    public static DocumentState getLocalState() {
        return instance.localState.get();
    }

    public static void setEncodingContext(Template template) {
        getInstance().localState.get().setTemplate(template);
    }

    public static Object get(Resolvable resolvable) {
        return getInstance().localState.get().get(resolvable);
    }

    public static Object get(String coordinates) {
        return getInstance().localState.get().get(coordinates);
    }

    class DocumentState implements State {
        private Template template;
        private Map<String, Object> valueCache = new HashMap<>();

        public void setTemplate(Template template) {
            this.template = template;
            valueCache.clear();
        }

        public void clear() {
            valueCache.clear();
        }

        @Override
        public Object get(String coordinates) throws DocumentNotMappedException {
            logger.trace("GET {}", coordinates);
            Object v = valueCache.get(coordinates);
            if (v == null) {
                v = template.getValue(coordinates);
                if (v == null) {
                    String parent = getParentCoordinates(coordinates);
                    if (parent == null) {
                        return null;
                    }
                    // this could be a nested resolvable, recurse up the tree to find it
                    v = get(parent);
                    if (v == null) {
                        throw new DocumentNotMappedException();
                    }
                }
                if (v instanceof Resolvable) {
                    v = ((Resolvable) v).resolve();
                }
                valueCache.put(coordinates, v);
                logger.trace("CREATING state entry for {} = {}", coordinates, v);
            }
            logger.trace("'{}' = {}", coordinates, v);
            return v;
        }

        private String getParentCoordinates(String coordinates) {
            int lastDotIdx = coordinates.lastIndexOf('.');
            if (lastDotIdx == -1) {
                return null;
            }
            return coordinates.substring(0, coordinates.length()-lastDotIdx-1);
        }

        @Override
        public Object get(Resolvable resolvable) throws DocumentNotMappedException {
            logger.trace("GET for resolvable {}", resolvable);
            if (template.isStateCachingRequired()) {
                String coordinates = template.getCoordinates(resolvable);
                // an embedded resolver with no coordinate in the template
                if (coordinates == null) {
                    return resolvable.resolve();
                }
                return get(coordinates);
            }
            return resolvable.resolve();
        }
    }

}
