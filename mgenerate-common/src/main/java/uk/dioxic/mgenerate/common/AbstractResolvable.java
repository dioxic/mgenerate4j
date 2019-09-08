package uk.dioxic.mgenerate.common;

public class AbstractResolvable<T> implements Resolvable<T>{

    protected Cache cache;

    @Override
    public T resolve() {
        return null;
    }

    @Override
    public T resolve(Cache cache) {
        return null;
    }
}
