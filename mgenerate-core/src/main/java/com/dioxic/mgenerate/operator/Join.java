package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

import java.util.List;

@Operator
public class Join implements Resolvable<String> {

    private static final String DEFAULT_SEP = "";

    @OperatorProperty(required = true)
    Resolvable<List<? extends CharSequence>> array;

    @OperatorProperty
    Resolvable<String> sep = OperatorFactory.wrap(DEFAULT_SEP);

    @Override
    public String resolve() {
        return String.join(sep.resolve(), array.resolve());
    }
}
