package uk.dioxic.mgenerate.core.operator.general;

import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.operator.type.UuidType;

import java.util.UUID;

@Operator
public class Uuid extends AbstractOperator<Object> {

    @OperatorProperty
    UuidType type = UuidType.BINARY;

    @Override
    public Object resolveInternal() {
        return type.toOutputType(UUID.randomUUID());
    }

}
