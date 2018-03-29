package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import org.bson.Document;
import org.bson.types.MinKey;

import static org.bson.assertions.Assertions.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@OperatorClass
public class Array implements Operator {
    private static final int DEFAULT_NUMBER = 5;

    @OperatorProperty(required = true)
    Operator of;

    @OperatorProperty
    Operator number = OperatorFactory.wrap(DEFAULT_NUMBER);

    public Array(){

    }

    public Array(Document document) {
        of = document.get("of", Operator.class);
        number = document.get("number", OperatorFactory.wrap(DEFAULT_NUMBER));
        notNull("of", of);
    }

    @Override
    public MinKey resolve() {
        return Stream.generate(() -> of.resolve())
                .limit(Operator.resolveInt(this.number))
                .collect(Collectors.toList());
    }

}
