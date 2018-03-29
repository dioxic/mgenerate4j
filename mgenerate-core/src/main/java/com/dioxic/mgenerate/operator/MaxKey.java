package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.annotation.OperatorClass;

@OperatorClass
public class MaxKey implements Operator<org.bson.types.MaxKey> {

    @Override
    public org.bson.types.MaxKey resolve() {
        return new org.bson.types.MaxKey();
    }
}
