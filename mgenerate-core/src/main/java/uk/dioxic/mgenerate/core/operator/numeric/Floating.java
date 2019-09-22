package uk.dioxic.mgenerate.core.operator.numeric;

import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator({"floating", "float", "double", "mgNumberDouble"})
public class Floating extends AbstractOperator<Double> {

    @OperatorProperty
    Double min = Double.MIN_VALUE;

    @OperatorProperty
    Double max = Double.MAX_VALUE;

    @Override
    public Double resolveInternal() {
        return FakerUtil.randomDouble(min, max);
    }

}
