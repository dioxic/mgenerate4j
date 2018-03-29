package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import org.bson.types.MinKey;

import java.util.concurrent.atomic.AtomicInteger;

@OperatorClass
public class Inc implements Operator, Initializable {

    @OperatorProperty
    Operator step = OperatorFactory.wrap(1);

    @OperatorProperty
    Operator start = OperatorFactory.wrap(0);

    private AtomicInteger counter;

    @Override
    public MinKey resolve() {
        return counter.getAndUpdate(n -> n + Operator.resolveInt(step));
    }

    @Override
    public void initialize() {
        counter = new AtomicInteger(Operator.resolveInt(start));
    }
}
