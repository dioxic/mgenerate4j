package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.annotation.OperatorClass;
import org.bson.types.MinKey;

@OperatorClass
public class MaxKey implements Operator {

    @Override
    public MinKey resolve() {
        return new org.bson.types.MaxKey();
    }
}
