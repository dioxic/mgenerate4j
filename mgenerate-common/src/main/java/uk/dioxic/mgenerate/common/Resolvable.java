package uk.dioxic.mgenerate.common;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface Resolvable<T> {

    T resolve();

    default T resolve(int depth) {
        return recursiveResolve(this, depth);
    }

    default T resolveFully() {
        return recursiveResolve(this, Integer.MAX_VALUE);
    }

    @SuppressWarnings("unchecked")
    static <T> T recursiveResolve(Resolvable<T> o) {
        return (T) recursiveResolveObject(o);
    }

    static Object recursiveResolveObject(Object o) {
        return recursiveResolveObject(o, 0, Integer.MAX_VALUE);
    }

    @SuppressWarnings("unchecked")
    static <T> T recursiveResolve(Resolvable<T> o, int depth) {
        return (T) recursiveResolveObject(o, 0, depth);
    }

    static Object recursiveResolveObject(Object o, int depth) {
        return recursiveResolveObject(o, 0, depth);
    }

    static Object recursiveResolveObject(Object o, int currentDepth, int maxDepth) {
        if (currentDepth == maxDepth) {
            return o;
        }

        if (o instanceof Resolvable) {
            Resolvable<?> r = (Resolvable<?>) o;
            return recursiveResolveObject(r.resolve(), currentDepth, maxDepth);
        }
        else if (o instanceof Document) {
            Document doc = new Document();
            ((Document) o).forEach((k, v) -> {
                v = recursiveResolveObject(v, currentDepth + 1, maxDepth);
                doc.put(k, v);
            });
            return doc;
        } else if (o instanceof Collection) {
            List<Object> l = new ArrayList<>((Collection<?>)o);

            for (int i = 0; i < l.size(); i++) {
                l.set(i, recursiveResolveObject(l.get(i), currentDepth + 1, maxDepth));
            }

            return l;
        }
        return o;
    }

}