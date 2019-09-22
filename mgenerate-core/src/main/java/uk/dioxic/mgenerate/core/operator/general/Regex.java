package uk.dioxic.mgenerate.core.operator.general;

import org.bson.BsonRegularExpression;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;

@Operator
public class Regex extends AbstractOperator<BsonRegularExpression> {

    @OperatorProperty
    Resolvable<String> string = Wrapper.wrap(".*");

    @OperatorProperty
    Resolvable<String> flags = Wrapper.wrap("");

    @Override
    public BsonRegularExpression resolveInternal() {
        return new BsonRegularExpression(string.resolve(), flags.resolve());
    }

}
