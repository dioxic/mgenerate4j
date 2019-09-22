package uk.dioxic.mgenerate.core.operator.mutator;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Operator
public class Concat extends AbstractOperator<List<?>> {

    @OperatorProperty(required = true)
    Resolvable<List<Object>> values;

    @Override
    public List<Object> resolveInternal() {
        return values
                .resolve()
                .stream()
                .flatMap(this::getStream)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private Stream<Object> getStream(Object o) {
        if (o instanceof List) {
            return ((List<Object>) o).stream();
        }
        if (o instanceof Resolvable) {
            return getStream(((Resolvable) o).resolve());
        }
        return Stream.of(o);
    }

}
