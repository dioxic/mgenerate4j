package uk.dioxic.mgenerate.operator.time;

import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;
import org.bson.BsonTimestamp;
import uk.dioxic.faker.resolvable.Resolvable;
import uk.dioxic.mgenerate.operator.NumberOpBuilder;

@Operator
public class Timestamp implements Resolvable<BsonTimestamp> {

    @OperatorProperty
    Resolvable<Integer> t = new NumberOpBuilder().build();

    @OperatorProperty
    Resolvable<Integer> i = new NumberOpBuilder().build();

    @Override
    public BsonTimestamp resolve() {
        return new BsonTimestamp(t.resolve(), i.resolve());
    }

}
