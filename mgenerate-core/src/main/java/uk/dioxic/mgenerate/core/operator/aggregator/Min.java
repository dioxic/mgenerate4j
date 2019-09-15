package uk.dioxic.mgenerate.core.operator.aggregator;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;

import java.util.List;

@Operator
public class Min implements Resolvable<Comparable> {

    @OperatorProperty(required = true)
    Resolvable<List<Comparable>> values;

    @Override
    @SuppressWarnings("unchecked")
    public Comparable resolve() {
        return values
                .resolve()
                .stream()
                .min(Comparable::compareTo)
                .orElse(null);
    }

}
