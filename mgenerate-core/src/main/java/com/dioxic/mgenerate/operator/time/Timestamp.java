package com.dioxic.mgenerate.operator.time;

import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import com.dioxic.mgenerate.operator.NumberBuilder;
import org.bson.BsonTimestamp;

@Operator
public class Timestamp implements Resolvable<BsonTimestamp> {

    @OperatorProperty
    Resolvable<Integer> t = new NumberBuilder().build();

    @OperatorProperty
    Resolvable<Integer> i = new NumberBuilder().build();

    @Override
    public BsonTimestamp resolve() {
        return new BsonTimestamp(t.resolve(), i.resolve());
    }

}
