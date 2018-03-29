package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import org.bson.types.MinKey;

import java.math.BigDecimal;
import java.math.BigInteger;

@OperatorClass
public class NumberDecimal implements Operator {

    private static final BigDecimal DEFAULT_MIN = BigDecimal.ZERO;
    private static final BigDecimal DEFAULT_MAX = BigDecimal.valueOf(1000);
    private static final BigDecimal DEFAULT_FIXED = BigDecimal.valueOf(1000);

    @OperatorProperty
    Operator min = OperatorFactory.wrap(DEFAULT_MIN);

    @OperatorProperty
    Operator max = OperatorFactory.wrap(DEFAULT_MAX);

    @Override
    public Integer resolve() {
        return FakerUtil.instance().number().numberBetween((Integer) min.resolve(), (Integer) max.resolve());
    }
}
