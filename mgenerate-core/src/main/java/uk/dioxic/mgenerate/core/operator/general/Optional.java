package uk.dioxic.mgenerate.core.operator.general;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;

@Operator
public class Optional extends AbstractOperator<Object> {

    @OperatorProperty(required = true)
    Resolvable<Object> value;

    @Override
    public Object resolveInternal() {
        return value.resolve();
    }

}
