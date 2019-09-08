package uk.dioxic.mgenerate.common;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public interface Resolvable<T> {

    T resolve();

    T resolve(Cache cache);

    static Object rescursiveResolve(Object o, Cache cache) {
        if (o instanceof Resolvable) {
            o = ((Resolvable) o).resolve(cache);
        }

        if (o instanceof Document) {
            Document doc = new Document();
            ((Document) o).forEach((k, v) -> {
                v = rescursiveResolve(v, cache);
                doc.put(k, v);
            });
            return doc;
        }
        else if (o instanceof List) {
            List<Object> l = new ArrayList(((List) o).size());
            ((List<?>) o).forEach(item -> {
                Object res = rescursiveResolve(item, cache);
                l.add(res);
            });
            return l;
        }
        return o;
    }

}