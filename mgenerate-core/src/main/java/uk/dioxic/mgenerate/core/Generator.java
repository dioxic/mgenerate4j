package uk.dioxic.mgenerate.core;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Cache;
import uk.dioxic.mgenerate.common.CacheResolvable;
import uk.dioxic.mgenerate.common.exception.DocumentNotMappedException;
import uk.dioxic.mgenerate.core.util.BsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Generator {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Document template;
    private Map<String, Object> variables;
    private Cache cache;

    // state
    int docLimit = 10;

    public Generator(Document template) {
        this.template = template;
        cache = new GeneratorCache(template);
    }

    public Flux<Document> generate() {
        long startMs = System.currentTimeMillis();
        //Flux<Document> flux =
        return Flux.range(0, docLimit)
                .map(i -> template)
                .flatMap(t -> Mono.just(genDoc(t)))
//                .doOnNext(doc -> {
//                    if (logger.isDebugEnabled()) {
//                        logger.debug(doc.toString());
//                        logger.debug(doc.toJson());
//                    }
//                })
                .doOnComplete(() -> {
                    long runtime = 1+ (System.currentTimeMillis() - startMs) / 1000;

                    logger.info("Runtime: {}s", runtime);
                    logger.info("Speed: {} docs/s", docLimit / runtime);
                });

//        return null;
//        return Flux.generate(
//                State::new,
//                (state, sink) -> {
//                    state.lastDoc = genDoc(template);
//                    state.count++;
//                    sink.next(state.lastDoc);
//
//                    if (state.count == docLimit) {
//                        sink.complete();
//                        System.out.println("Runtime: " + state.runtime() + "s");
//                        System.out.println("Speed: " + state.speed() + " docs/s");
//                    }
//
//                    valueCache.clear();
//
//                    return state;
//                });
    }

    /**
     * is there a resolvable in the object? accounts for document and list traversal
     * @param o
     * @return
     */
    private static boolean isMutable(Object o) {
        if (o instanceof Map) {
            return isMutable(((Map) o).values());
        }
        else if (o instanceof Iterable) {
            for (Object val : (Iterable)o) {
                if (isMutable(val))
                    return true;
            }
        }
        else return (o instanceof Resolvable);

        return false;
    }

    private Document genDoc(Document template) {
        return (Document)generate(template, null, null);
    }

    private static String getCoordinates(String leaf, String key) {
        if (key == null) {
            return null;
        }
        if (leaf != null) {
            return leaf + "." + key;
        }
        return key;
    }

    public Object generate(Object o, String leaf, String key) {
        String coordinates = getCoordinates(leaf, key);

        if (o instanceof Document) {
            Document d = (Document)o;

            if (isMutable(d)) {
                Document clone = new Document();
                d.forEach((k, v) -> clone.put(k, generate(v, coordinates, k)));
                d = clone;
            }
            return d;
        }
        else if (o instanceof List) {
            List<?> l = (List)o;
            if (isMutable(o)) {
                List<Object> clone = new ArrayList<>(l.size());
                for (int idx=0; idx<l.size(); idx++) {
                    clone.add(generate(l.get(idx), coordinates, Integer.toString(idx)));
                }
                l = clone;
            }
            return l;
        }
        else if (o instanceof Resolvable) {
            return cache.get(coordinates);
        }
        return o;
    }

    static class State {
        int count = 0;
        Document lastDoc;
        long startMs = System.currentTimeMillis();

        long runtime() {
            return (System.currentTimeMillis() - startMs) / 1000;
        }

        long speed() {
            return count / Math.max(runtime(), 1);
        }
    }

    static class GeneratorCache implements Cache {
        private final Logger logger = LoggerFactory.getLogger(this.getClass());
        private Map<String, Object> templateFlatMap;
        private Map<String, Object> valueCache = new HashMap<>();

        GeneratorCache(Document template) {
            this.templateFlatMap = BsonUtil.flatMap(template);
        }

        public void clear() {
            valueCache.clear();
        }

        @Override
        public Object get(String coordinates) throws DocumentNotMappedException {
            logger.debug("GET {}", coordinates);
            Object v = valueCache.get(coordinates);
            if (v == null) {
                v = templateFlatMap.get(coordinates);
                if (v instanceof CacheResolvable) {
                    v = ((CacheResolvable)v).resolve(this);
                    valueCache.put(coordinates, v);
                }
                else if (v instanceof Resolvable) {
                    v = ((Resolvable)v).resolve();
                    valueCache.put(coordinates, v);
                }
            }
            return v;
        }

    }


}
