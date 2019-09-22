package uk.dioxic.mgenerate.core.operator.numeric;

import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator({"integer", "number", "int", "mgNumberInt", "int32"})
public class NumberInt extends AbstractOperator<Integer> {

    @OperatorProperty
    Integer min = Integer.MIN_VALUE;

    @OperatorProperty
    Integer max = Integer.MAX_VALUE;

    @Override
    public Integer resolveInternal() {
        return FakerUtil.numberBetween(min, max);
    }

}
