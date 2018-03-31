package org.bson;

import com.dioxic.mgenerate.Resolvable;

@Deprecated
public class BsonOperator extends BsonValue {

    private Resolvable operator;

    public BsonOperator(Resolvable operator) {
        this.operator = operator;
    }

    @Override
    public BsonType getBsonType() {
        return BsonType.STRING;
    }
}
