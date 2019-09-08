package uk.dioxic.mgenerate.core.operator;

import uk.dioxic.mgenerate.common.State;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;

@Operator
public class Mod implements Resolvable<Integer> {

    @OperatorProperty(required = true)
    Resolvable<Integer> input;

    @OperatorProperty
    Integer mod = 720;

    @Override
    public Integer resolve() {
        return Math.floorMod(input.resolve(), mod);
    }
}
