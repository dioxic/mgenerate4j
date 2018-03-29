package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import org.bson.Document;
import org.bson.types.MinKey;

@OperatorClass
public class Number implements Operator {

    private static final int DEFAULT_MIN = Integer.MIN_VALUE;
    private static final int DEFAULT_MAX = Integer.MAX_VALUE;

    @OperatorProperty
    Operator min = OperatorFactory.wrap(DEFAULT_MIN);

    @OperatorProperty
    Operator max = OperatorFactory.wrap(DEFAULT_MAX);

    @Override
    public Integer resolve() {
        return FakerUtil.instance().number().numberBetween((Integer) min.resolve(), (Integer) max.resolve());
    }
}
