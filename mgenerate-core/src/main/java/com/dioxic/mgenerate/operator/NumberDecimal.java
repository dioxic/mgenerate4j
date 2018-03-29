package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;

import java.math.BigDecimal;

@OperatorClass
public class NumberDecimal implements Operator<BigDecimal> {

    private static final Long DEFAULT_MIN = 0L;
    private static final Long DEFAULT_MAX = 1000L;
    private static final Operator<Integer> DEFAULT_FIXED = OperatorFactory.wrap(Integer.valueOf(2));

    @OperatorProperty
    Long min = DEFAULT_MIN;

    @OperatorProperty
    Long max = DEFAULT_MAX;

    @OperatorProperty
    Operator<Integer> fixed = DEFAULT_FIXED;

    @Override
    public BigDecimal resolve() {
        return FakerUtil.randomDecimal(min, max, fixed.resolve());
    }
}
