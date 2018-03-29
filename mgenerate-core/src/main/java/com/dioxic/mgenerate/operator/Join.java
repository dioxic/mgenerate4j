package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import org.bson.types.MinKey;

@OperatorClass
public class Join implements Operator {

    private static final String DEFAULT_SEP = "";

    @OperatorProperty(required = true)
    Operator array;

    @OperatorProperty
    Operator sep = OperatorFactory.wrap(DEFAULT_SEP);

    @Override
    public MinKey resolve() {
        return String.join(Operator.resolveString(sep), Operator.resolveList(array, String.class));
    }
}
