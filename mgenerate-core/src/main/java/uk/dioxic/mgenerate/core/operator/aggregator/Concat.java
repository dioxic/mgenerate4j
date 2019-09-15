package uk.dioxic.mgenerate.core.operator.aggregator;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Operator
public class Concat implements Resolvable<List> {

    @OperatorProperty(required = true)
    Resolvable<List<Object>> values;

    @Override
    public List resolve() {
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
