package uk.dioxic.mgenerate.core.operator.aggregator;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;

@Operator
public class Inc implements Resolvable<Number> {

    @OperatorProperty(required = true)
    Resolvable<Number> input;

    @OperatorProperty
    Resolvable<Number> step = Wrapper.wrap(1);

    @Override
    public Number resolve() {
        Number in = input.resolve();
        Number s = step.resolve();

        if (Double.class.isAssignableFrom(in.getClass())) {
            return in.doubleValue() + s.doubleValue();
        }
        if (Long.class.isAssignableFrom(in.getClass())) {
            return in.longValue() + s.longValue();
        }
        if (Integer.class.isAssignableFrom(in.getClass())) {
            return in.intValue() + s.intValue();
        }

        return null;
    }
}
