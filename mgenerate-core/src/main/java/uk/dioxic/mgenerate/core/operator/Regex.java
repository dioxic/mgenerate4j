package uk.dioxic.mgenerate.core.operator;

import org.bson.BsonRegularExpression;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;

@Operator
public class Regex implements Resolvable<BsonRegularExpression> {

    @OperatorProperty
    Resolvable<String> string = Wrapper.wrap(".*");

    @OperatorProperty
    Resolvable<String> flags = Wrapper.wrap("");

    @Override
    public BsonRegularExpression resolve() {
        return new BsonRegularExpression(string.resolve(), flags.resolve());
    }

}
