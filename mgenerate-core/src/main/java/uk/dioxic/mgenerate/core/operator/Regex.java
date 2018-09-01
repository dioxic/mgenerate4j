package uk.dioxic.mgenerate.core.operator;

import uk.dioxic.mgenerate.common.OperatorFactory;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
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
