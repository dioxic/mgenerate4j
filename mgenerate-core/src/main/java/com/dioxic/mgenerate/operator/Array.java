package com.dioxic.mgenerate.operator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.BuilderProperty;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import org.bson.Document;

@OperatorClass
public class Array implements Operator {
    private static final int DEFAULT_NUMBER = 5;

    @OperatorProperty(required = true)
    Operator of;

    @OperatorProperty
    Operator number = OperatorFactory.wrap(DEFAULT_NUMBER);

    @Override
    public List<?> resolve() {
        return Stream.generate(() -> of.resolve())
                .limit(Operator.resolveInt(this.number))
                .collect(Collectors.toList());
    }

}
