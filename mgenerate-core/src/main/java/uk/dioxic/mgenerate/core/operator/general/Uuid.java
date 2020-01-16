package uk.dioxic.mgenerate.core.operator.general;

import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;

import java.util.UUID;

@Operator
public class Uuid extends AbstractOperator<Object> {

    @OperatorProperty
    Boolean asString = Boolean.FALSE;

    @Override
    public Object resolveInternal() {
        UUID uuid = UUID.randomUUID();
        return asString ? uuid.toString() : uuid;
    }

}
