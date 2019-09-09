package uk.dioxic.mgenerate.core.operator.numeric;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator({"number", "int"})
public class NumberInt implements Resolvable<Integer> {

    @OperatorProperty
    Integer min = Integer.MIN_VALUE;

    @OperatorProperty
    Integer max = Integer.MAX_VALUE;

    @Override
    public Integer resolve() {
        return FakerUtil.numberBetween(min, max);
    }

}
