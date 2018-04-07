package uk.dioxic.mgenerate.operator;

import uk.dioxic.faker.resolvable.Resolvable;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;

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
