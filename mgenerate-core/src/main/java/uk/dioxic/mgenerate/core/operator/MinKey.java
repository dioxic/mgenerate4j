package uk.dioxic.mgenerate.core.operator;

import uk.dioxic.mgenerate.common.Cache;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;

@Operator
public class MinKey implements Resolvable<org.bson.types.MinKey> {

    @Override
    public org.bson.types.MinKey resolve() {
        return new org.bson.types.MinKey();
    }

    @Override
    public org.bson.types.MinKey resolve(Cache cache) {
        return resolve();
    }
}
