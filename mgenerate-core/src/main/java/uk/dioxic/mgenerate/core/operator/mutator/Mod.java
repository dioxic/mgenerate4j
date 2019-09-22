package uk.dioxic.mgenerate.core.operator.mutator;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;

@Operator
public class Mod extends AbstractOperator<Integer> {

    @OperatorProperty(required = true)
    Resolvable<Number> input;

    @OperatorProperty
    Integer mod = 720;

    @Override
    public Integer resolveInternal() {
        return Math.floorMod(input.resolve().intValue(), mod);
    }
}
