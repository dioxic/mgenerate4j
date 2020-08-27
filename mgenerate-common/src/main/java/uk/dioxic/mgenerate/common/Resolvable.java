package uk.dioxic.mgenerate.common;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public interface Resolvable<T> {

    T resolve();

    @SuppressWarnings("unchecked")
    static <T> T recursiveResolve(Resolvable<T> o) {
        return (T) recursiveResolveObject(o);
    }

    static Object recursiveResolveObject(Object o) {
        if (o instanceof Resolvable) {
            Resolvable<?> r = (Resolvable<?>) o;
            o = r.resolve();
        }

        if (o instanceof Document) {
            Document doc = new Document();
            ((Document) o).forEach((k, v) -> {
                v = recursiveResolveObject(v);
                doc.put(k, v);
            });
            return doc;
        }
        else if (o instanceof List) {
            List<Object> l = new ArrayList<>((List<?>)o);

            for (int i=0; i< l.size(); i++) {
                l.set(i, recursiveResolveObject(l.get(i)));
            }

            return l;
        }
        return o;
    }

}