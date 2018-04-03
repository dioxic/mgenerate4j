package uk.dioxic.mgenerate.operator;

import uk.dioxic.mgenerate.FakerUtil;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Number implements Resolvable<Integer> {

    @OperatorProperty
    Integer min = Integer.MIN_VALUE;

    @OperatorProperty
    Integer max = Integer.MAX_VALUE;

    @Override
    public Integer resolve() {
        return FakerUtil.numberBetween(min, max);
    }
}
