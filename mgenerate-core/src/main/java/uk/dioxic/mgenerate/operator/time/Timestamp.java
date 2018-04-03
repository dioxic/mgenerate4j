package uk.dioxic.mgenerate.operator.time;

import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;
import org.bson.BsonTimestamp;
import uk.dioxic.faker.resolvable.Resolvable;
import uk.dioxic.mgenerate.operator.NumberBuilder;

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
