package uk.dioxic.mgenerate.core.operator.convert;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;

@Operator
public class toLong extends AbstractOperator<Long> {

    @OperatorProperty(required = true)
    Resolvable<Object> input;

    @Override
    protected Long resolveInternal() {
        Object o = input.resolve();

        if (o instanceof Number) {
            return ((Number) o).longValue();
        }
        if (o instanceof String) {
            return Long.parseLong(o.toString());
        }

        return null;
    }
}
