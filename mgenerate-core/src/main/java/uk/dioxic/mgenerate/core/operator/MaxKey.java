package uk.dioxic.mgenerate.core.operator;

import uk.dioxic.mgenerate.common.Cache;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;

@Operator
public class MaxKey implements Resolvable<org.bson.types.MaxKey> {

    @Override
    public org.bson.types.MaxKey resolve() {
        return new org.bson.types.MaxKey();
    }

    @Override
    public org.bson.types.MaxKey resolve(Cache cache) {
        return resolve();
    }
}
