package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.annotation.OperatorClass;

@OperatorClass
public class MinKey implements Operator {

    @Override
    public org.bson.types.MinKey resolve() {
        return new org.bson.types.MinKey();
    }
}
