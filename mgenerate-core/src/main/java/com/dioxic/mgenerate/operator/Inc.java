package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.Initializable;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;

import java.util.concurrent.atomic.AtomicInteger;

@Operator
public class Inc implements Resolvable<Integer>, Initializable {

    @OperatorProperty
    Resolvable<Integer> step = OperatorFactory.wrap(1);

    @OperatorProperty
    Integer start = 0;

    private AtomicInteger counter;

    @Override
    public Integer resolve() {
        return counter.getAndUpdate(n -> n + step.resolve());
    }

    @Override
    public void initialize() {
        counter = new AtomicInteger(start);
    }
}
