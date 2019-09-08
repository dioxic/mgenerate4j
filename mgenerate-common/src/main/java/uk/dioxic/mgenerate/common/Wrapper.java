package uk.dioxic.mgenerate.common;

import uk.dioxic.mgenerate.common.exception.WrapException;

public class Wrapper<T> implements Resolvable<T> {

    private T value;
    private Resolvable resolvable;
    private Transformer<T> transformer;

    public static <T> Resolvable<T> wrap(T value) {
        return (value instanceof Resolvable) ? (Resolvable) value : new Wrapper<>(value);
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
            resolvable = (Resolvable)value;
        }
        else {
            this.value = transformer.transform(value);
        }

        this.transformer = transformer;
    }

    @Override
    public T resolve() {
        return resolve(null);
    }

    @Override
    public T resolve(Cache cache) {
        Object res = value;
        if (resolvable != null) {
            res = Resolvable.rescursiveResolve(resolvable, cache);
        }
        if (transformer != null) {
            res = transformer.transform(res);
        }

        return (T)res;
    }
}
