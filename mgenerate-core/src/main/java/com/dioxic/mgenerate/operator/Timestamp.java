package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import org.bson.BsonTimestamp;

@OperatorClass
public class Timestamp implements Operator<BsonTimestamp> {

    @OperatorProperty
    Operator<Integer> t = new NumberBuilder().build();

    @OperatorProperty
    Operator<Integer> i = new NumberBuilder().build();

    @Override
    public BsonTimestamp resolve() {
        return new BsonTimestamp(t.resolve(), i.resolve());
    }

}
