package uk.dioxic.mgenerate.core.operator.convert;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;

@Operator
public class toBinary extends AbstractOperator<String> {

    @OperatorProperty(required = true)
    Resolvable<Number> input;

    @Override
    protected String resolveInternal() {
        Number num = input.resolve();

        if (num instanceof Long) {
            return Long.toBinaryString((Long)num);
        }
        if (num instanceof Integer) {
            return Integer.toBinaryString((Integer)num);
        }

        return null;
    }
}
