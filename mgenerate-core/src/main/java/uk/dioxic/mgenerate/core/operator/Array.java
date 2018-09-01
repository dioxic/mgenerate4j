package uk.dioxic.mgenerate.core.operator;

import uk.dioxic.mgenerate.common.OperatorFactory;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Operator
public class Array implements Resolvable<List> {
    private static final int DEFAULT_NUMBER = 5;

    @OperatorProperty(required = true)
    Resolvable of;

    @OperatorProperty
    Resolvable<Integer> number = OperatorFactory.wrap(DEFAULT_NUMBER);

    @Override
    public List<Object> resolve() {
        return Stream.generate(() -> of.resolve())
                .limit(number.resolve())
                .collect(Collectors.toList());
    }

}
