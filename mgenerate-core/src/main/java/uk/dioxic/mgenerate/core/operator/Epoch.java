package uk.dioxic.mgenerate.core.operator;

import uk.dioxic.faker.resolvable.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;

import java.lang.Number;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;

@Operator
public class Epoch implements Resolvable<Number> {

    @OperatorProperty(required = true)
    Resolvable<LocalDateTime> input;

    @OperatorProperty
    ChronoField chronoField;

    @Override
    public Number resolve() {
        LocalDateTime ldt = input.resolve();

        if (chronoField == null) {
            return (int) (input.resolve().toInstant(ZoneOffset.UTC).toEpochMilli() & Integer.MAX_VALUE);
        }

        if (chronoField.range().getMaximum() > Integer.MAX_VALUE) {
            return ldt.getLong(chronoField);
        }
        else {
            return ldt.get(chronoField);
        }
    }
}
