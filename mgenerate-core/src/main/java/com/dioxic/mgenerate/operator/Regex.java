package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import org.bson.BsonRegularExpression;

@OperatorClass
public class Regex implements Operator<BsonRegularExpression> {

    @OperatorProperty
    Operator<String> string = OperatorFactory.wrap(".*");

    @OperatorProperty
    Operator<String> flags = OperatorFactory.wrap("");

    @Override
    public BsonRegularExpression resolve() {
        return new BsonRegularExpression(string.resolve(), flags.resolve());
    }

}
