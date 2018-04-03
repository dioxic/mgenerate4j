package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class MinKey implements Resolvable<org.bson.types.MinKey> {

    @Override
    public org.bson.types.MinKey resolve() {
        return new org.bson.types.MinKey();
    }
}
