package uk.dioxic.mgenerate.common;

import uk.dioxic.mgenerate.common.exception.WrapException;

public class Wrapper<T> implements CacheResolvable<T> {

    private T value;
    private Resolvable resolvable;
    private Transformer<T> transformer;

    public static <T> Resolvable<T> wrap(T value) {
        return (value instanceof Resolvable) ? (Resolvable)value : new Wrapper<>(value);
    }

    public static <T> Resolvable<T> wrap(Object object, Class<T> desiredType, TransformerRegistry transformRegistry) {
        if (object != null) {
            if (transformRegistry.canHandle(desiredType)) {
                return new Wrapper<>(object, transformRegistry.get(desiredType));
            }
            if (desiredType.isAssignableFrom(object.getClass())) {
                return new Wrapper<>((T) object);
            }
            if (object instanceof Resolvable) {
                return (Resolvable) object;
            }
            throw new WrapException(object.getClass(), desiredType);
        }
        return null;
    }

    private Wrapper(T value) {
        this.value = value;
    }

    private Wrapper(Object value, Transformer<T> transformer) {
        if (value instanceof Resolvable) {
            this.resolvable = (Resolvable) value;
        }
        else {
            this.value = transformer.transform(value);
        }

        this.transformer = transformer;
    }

    @Override
    public T resolve() {
        if (value != null) {
            return value;
        }

        return transformer.transform(resolvable.resolve());
    }

    @Override
    public T resolve(Cache cache) {
        if (value != null) {
            return value;
        }

        return transformer.transform(CacheResolvable.resolve(cache, resolvable));
    }
}
