package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

import java.math.BigDecimal;

@Operator
public class NumberDecimal implements Resolvable<BigDecimal> {

    private static final Long DEFAULT_MIN = 0L;
    private static final Long DEFAULT_MAX = 1000L;
    private static final Resolvable<Integer> DEFAULT_FIXED = OperatorFactory.wrap(Integer.valueOf(2));

    @OperatorProperty
    Long min = DEFAULT_MIN;

    @OperatorProperty
    Long max = DEFAULT_MAX;

    @OperatorProperty
    Resolvable<Integer> fixed = DEFAULT_FIXED;

    @Override
    public BigDecimal resolve() {
        return FakerUtil.randomDecimal(min, max, fixed.resolve());
    }
}
