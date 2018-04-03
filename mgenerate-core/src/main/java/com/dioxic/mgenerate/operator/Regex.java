package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;
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
