package uk.dioxic.mgenerate.core.operator.general;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;

import java.util.UUID;

@Operator
public class Uuid implements Resolvable<UUID> {

    @Override
    public UUID resolve() {
        return UUID.randomUUID();
    }

}
