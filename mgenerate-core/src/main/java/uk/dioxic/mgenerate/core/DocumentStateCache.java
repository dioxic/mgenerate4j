package uk.dioxic.mgenerate.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.State;
import uk.dioxic.mgenerate.common.exception.DocumentNotMappedException;
import uk.dioxic.mgenerate.core.util.DocumentUtil;

import java.util.HashMap;
import java.util.Map;

public class DocumentStateCache {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static DocumentStateCache instance = new DocumentStateCache();
    private ThreadLocal<DocumentState> localState = ThreadLocal.withInitial(() -> {
        DocumentState state = new DocumentState();
        logger.trace("creating thread local document state {}", state.hashCode());
        return state;
    });

    public static DocumentStateCache getInstance() {
        return instance;
    }

    private DocumentStateCache() {
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

        void setTemplate(Template template) {
            logger.trace("setting template for document state {}", this.hashCode());
            this.template = template;
            valueCache.clear();
        }

        public void clear() {
            valueCache.clear();
        }

        private String getNearestParent(String coordinates) throws DocumentNotMappedException {
            String parent = getParentCoordinates(coordinates);

            if (parent == null) {
                return null;
            }

            if (!valueCache.containsKey(parent) && !template.containsKey(parent)) {
                return getNearestParent(parent);
            }
            logger.trace("nearest parent for {} = {}", coordinates, parent);
            return parent;
        }

        public void put(String coordinates, Object value) {
            valueCache.put(coordinates, value);
        }

        public void put(Resolvable resolvable, Object value) {
            valueCache.put(template.getCoordinates(resolvable), value);
        }

        @Override
        public Object get(String coordinates) throws DocumentNotMappedException {
            logger.trace("GET {}", coordinates);
            Object v = valueCache.get(coordinates);
            if (v == null) {
                v = template.get(coordinates);
                if (v == null) {
                    String parentCoordinates = getNearestParent(coordinates);
                    if (parentCoordinates != null) {
                        v = DocumentUtil.coordinateLookup(coordinates.substring(parentCoordinates.length() + 1), get(parentCoordinates));
                    }
                }
                // make sure anything stored in the value cache is flatmapped and fully hydrated
                logger.trace("CREATING state entry for {}", coordinates);
                v = DocumentUtil.flatMap(valueCache, coordinates, true, v);
            }
            logger.trace("RETURNED {} = {}", coordinates, v);
            return v;
        }

        private String getParentCoordinates(String coordinates) {
            int lastDotIdx = coordinates.lastIndexOf('.');
            if (lastDotIdx == -1) {
                return null;
            }
            return coordinates.substring(0, lastDotIdx);
        }

        @Override
        public Object get(Resolvable resolvable) throws DocumentNotMappedException {
            logger.trace("GET for resolvable {}", resolvable);
            if (template.isStateCachingRequired()) {
                String coordinates = template.getCoordinates(resolvable);
                // embedded resolvers will not have any coordinates in the template
                if (coordinates != null) {
                    return get(coordinates);
                }
            }
            return resolvable.resolve();
        }
    }

}
