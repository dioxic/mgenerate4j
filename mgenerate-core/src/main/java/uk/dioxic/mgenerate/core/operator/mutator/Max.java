package uk.dioxic.mgenerate.core.operator.mutator;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;

import java.util.List;

@Operator
public class Max extends AbstractOperator<Comparable> {

    @OperatorProperty(required = true)
    Resolvable<List<Comparable>> values;

    @Override
    @SuppressWarnings("unchecked")
    public Comparable resolveInternal() {
        return values
                .resolve()
                .stream()
                .max(Comparable::compareTo)
                .orElse(null);
    }

}
