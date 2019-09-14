package uk.dioxic.mgenerate.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.State;
import uk.dioxic.mgenerate.common.exception.DocumentNotMappedException;
import uk.dioxic.mgenerate.core.util.BsonUtil;

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

        private String getNearestParent(String coordinates) throws DocumentNotMappedException {
            String parent = getParentCoordinates(coordinates);

            if (parent == null) {
                return null;
            }

            Object v = valueCache.get(parent);
            if (v == null) {
                v = template.getValue(parent);
                if (v == null) {
                    return getNearestParent(parent);
                }
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
                v = template.getValue(coordinates);
                if (v == null) {
                    String parentCoordinates = getNearestParent(coordinates);
                    if (parentCoordinates != null) {
                        v = BsonUtil.resolveCoordinate(coordinates.substring(parentCoordinates.length()+1), get(parentCoordinates));
                    }
                    else {
                        throw new DocumentNotMappedException();
                    }
                }
                // make sure anything stored in the value cache is fully hydrated
                v = BsonUtil.recursiveResolve(v);
                BsonUtil.map(valueCache, coordinates, v);
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
            return coordinates.substring(0, lastDotIdx);
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