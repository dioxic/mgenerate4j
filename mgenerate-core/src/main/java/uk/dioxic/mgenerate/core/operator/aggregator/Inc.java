package uk.dioxic.mgenerate.core.operator.aggregator;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;

import java.time.LocalDateTime;

@Operator
public class Inc implements Resolvable<Number> {

    @OperatorProperty(required = true)
    Resolvable<Number> input;

    @OperatorProperty
    Resolvable<Number> step = Wrapper.wrap(1);

    @Override
    public Number resolve() {
        Number input = this.input.resolve();
        Number step = this.step.resolve();

        if (Double.class.isAssignableFrom(input.getClass())) {
            return input.doubleValue() + step.doubleValue();
        }
        if (Long.class.isAssignableFrom(input.getClass())) {
            return input.longValue() + step.longValue();
        }
        if (Integer.class.isAssignableFrom(input.getClass())) {
            return input.intValue() + step.intValue();
        }

        throw new UnsupportedOperationException("unsupported $inc : { input: " + input.getClass() + ", step: " + input.getClass() + " }");
    }
}
