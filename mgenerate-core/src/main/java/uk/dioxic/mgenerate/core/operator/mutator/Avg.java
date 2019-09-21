package uk.dioxic.mgenerate.core.operator.mutator;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.common.exception.ResolveException;

import java.util.List;

@Operator
public class Avg implements Resolvable<Number> {

    @OperatorProperty(required = true)
    Resolvable<List<Number>> values;

    @Override
    public Number resolve() {
        List<Number> numbers = values.resolve();
        Class<? extends Number> firstClass = numbers.get(0).getClass();

        if (Double.class.isAssignableFrom(firstClass)) {
            return numbers.stream().mapToDouble(Number::doubleValue).average().orElse(0);
        }
        if (Long.class.isAssignableFrom(firstClass)) {
            return numbers.stream().mapToLong(Number::longValue).average().orElse(0);
        }
        if (Integer.class.isAssignableFrom(firstClass)) {
            return numbers.stream().mapToInt(Number::intValue).average().orElse(0);
        }

        throw new ResolveException("cannot calculate average for type of " + firstClass.getSimpleName());
    }

}
