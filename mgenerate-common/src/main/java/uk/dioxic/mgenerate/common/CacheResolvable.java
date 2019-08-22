package uk.dioxic.mgenerate.common;

public interface CacheResolvable<T> extends Resolvable<T> {

    T resolve(Cache cache);

    static <T> T resolve(Cache cache, Resolvable<T> resolvable) {
        if (resolvable instanceof CacheResolvable) {
            return ((CacheResolvable<T>) resolvable).resolve(cache);
        }
        return resolvable.resolve();
    }
}
