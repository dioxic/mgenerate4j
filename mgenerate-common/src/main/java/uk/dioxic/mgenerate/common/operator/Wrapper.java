package uk.dioxic.mgenerate.common.operator;

import uk.dioxic.mgenerate.common.transformer.Transformer;
import uk.dioxic.faker.resolvable.Resolvable;

public class Wrapper<T> implements Resolvable<T> {

    private T value;
    private Resolvable resolvable;
    private Transformer<T> transformer;

    public Wrapper(T value) {
        this.value = value;
    }

    public Wrapper(Object value, Transformer<T> transformer) {
        if (value instanceof Resolvable){
            this.resolvable = (Resolvable)value;
        }
        else{
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

}
