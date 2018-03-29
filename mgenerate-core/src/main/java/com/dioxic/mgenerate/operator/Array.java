package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.bson.assertions.Assertions.notNull;

@OperatorClass
public class Array implements Operator<List> {
    private static final int DEFAULT_NUMBER = 5;

    @OperatorProperty(required = true)
    Operator of;

    @OperatorProperty
    Operator<Integer> number = OperatorFactory.wrap(DEFAULT_NUMBER);

    @Override
    public List<Object> resolve() {
        return Stream.generate(() -> of.resolve())
                .limit(number.resolve())
                .collect(Collectors.toList());
    }

}
