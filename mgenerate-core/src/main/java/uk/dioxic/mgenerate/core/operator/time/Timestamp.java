package uk.dioxic.mgenerate.core.operator.time;

import org.bson.BsonTimestamp;
import uk.dioxic.mgenerate.common.Cache;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.NumberInt;

@Operator
public class Timestamp implements Resolvable<BsonTimestamp> {

    @OperatorProperty
    Resolvable<Integer> t = new NumberInt();

    @OperatorProperty
    Resolvable<Integer> i = new NumberInt();

    @Override
    public BsonTimestamp resolve() {
        return resolve(null);
    }

    @Override
    public BsonTimestamp resolve(Cache cache) {
        return new BsonTimestamp(t.resolve(cache), i.resolve(cache));
    }


}
