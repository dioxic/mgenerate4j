package uk.dioxic.mgenerate.core.operator;

import uk.dioxic.mgenerate.common.Cache;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Operator
public class Array implements Resolvable<List> {
    private static final int DEFAULT_NUMBER = 5;

    @OperatorProperty(required = true)
    Resolvable of;

    @OperatorProperty
    Resolvable<Integer> number = Wrapper.wrap(DEFAULT_NUMBER);

    @Override
    public List<Object> resolve(Cache cache) {
        return Stream.generate(() -> of.resolve(cache))
                .limit(number.resolve(cache))
                .collect(Collectors.toList());
    }

    @Override
    public List<Object> resolve() {
        return resolve(null);
    }

}
