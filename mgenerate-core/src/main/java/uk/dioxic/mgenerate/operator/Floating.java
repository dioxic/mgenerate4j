package uk.dioxic.mgenerate.operator;

import uk.dioxic.mgenerate.FakerUtil;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Floating implements Resolvable<Double> {

    @OperatorProperty
    Double min = Double.MIN_VALUE;

    @OperatorProperty
    Double max = Double.MAX_VALUE;

    @Override
    public Double resolve() {
        return FakerUtil.randomDouble(min, max);
    }
}
