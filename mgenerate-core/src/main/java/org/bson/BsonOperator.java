package org.bson;

import com.dioxic.mgenerate.operator.Operator;

@Deprecated
public class BsonOperator extends BsonValue {

    private Operator operator;

    public BsonOperator(Operator operator) {
        this.operator = operator;
    }

    @Override
    public BsonType getBsonType() {
        return BsonType.STRING;
    }
}
