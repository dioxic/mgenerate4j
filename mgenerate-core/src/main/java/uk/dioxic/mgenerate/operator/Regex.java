package uk.dioxic.mgenerate.operator;

import uk.dioxic.mgenerate.OperatorFactory;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;
import org.bson.BsonRegularExpression;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Regex implements Resolvable<BsonRegularExpression> {

    @OperatorProperty
    Resolvable<String> string = OperatorFactory.wrap(".*");

    @OperatorProperty
    Resolvable<String> flags = OperatorFactory.wrap("");

    @Override
    public BsonRegularExpression resolve() {
        return new BsonRegularExpression(string.resolve(), flags.resolve());
    }

}
