package uk.dioxic.mgenerate.core.operator.mutator;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;

import java.util.List;
import java.util.stream.Collectors;

@Operator
public class Distinct implements Resolvable<List<Object>> {

    @OperatorProperty(required = true)
    Resolvable<List<Object>> values;

    @Override
    public List<Object> resolve() {
        return values
                .resolve()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

}
