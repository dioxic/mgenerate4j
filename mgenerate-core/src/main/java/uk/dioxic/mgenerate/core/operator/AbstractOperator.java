package uk.dioxic.mgenerate.core.operator;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

public abstract class AbstractOperator<T> implements Resolvable<T> {

    private Resolvable<Object> isNull = Wrapper.wrap(false);

    public final boolean isNull() {
        Object o = isNull.resolve();

        if (o instanceof Number) {
            return ((Number)o).doubleValue() > FakerUtil.random().nextDouble();
        }
        if (o instanceof Boolean) {
            return (Boolean)o;
        }

        return o == null;
    }

    @OperatorProperty
    public void setIsNull(Resolvable<Object> isNull) {
        this.isNull = isNull;
    }

    protected abstract T resolveInternal();

    @Override
    public final T resolve() {
        return isNull() ? null : resolveInternal();
    }
}
