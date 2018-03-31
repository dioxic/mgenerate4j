package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;

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
