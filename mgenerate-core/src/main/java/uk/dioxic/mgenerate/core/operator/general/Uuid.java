package uk.dioxic.mgenerate.core.operator.general;

import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;

import java.util.UUID;

@Operator
public class Uuid extends AbstractOperator<UUID> {

    @Override
    public UUID resolveInternal() {
        return UUID.randomUUID();
    }

}
