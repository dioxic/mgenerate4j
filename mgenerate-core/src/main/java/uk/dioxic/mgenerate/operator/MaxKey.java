package uk.dioxic.mgenerate.operator;

import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class MaxKey implements Resolvable<org.bson.types.MaxKey> {

    @Override
    public org.bson.types.MaxKey resolve() {
        return new org.bson.types.MaxKey();
    }
}
