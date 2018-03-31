package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Operator
public class Array implements Resolvable<List> {
    private static final int DEFAULT_NUMBER = 5;

    @OperatorProperty(required = true)
    Resolvable of;

    @OperatorProperty
    Resolvable<Integer> number = OperatorFactory.wrap(DEFAULT_NUMBER);

    @Override
    public List<Object> resolve() {
        return Stream.generate(() -> of.resolve())
                .limit(number.resolve())
                .collect(Collectors.toList());
    }

}
