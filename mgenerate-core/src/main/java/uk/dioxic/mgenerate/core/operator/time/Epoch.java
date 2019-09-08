package uk.dioxic.mgenerate.core.operator.time;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.State;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;

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
        return format(input.resolve());
    }
    private Number format(LocalDateTime ldt) {
        if (chronoField == null) {
            return (int) (ldt.toInstant(ZoneOffset.UTC).toEpochMilli() & Integer.MAX_VALUE);
        }

        if (chronoField.range().getMaximum() > Integer.MAX_VALUE) {
            return ldt.getLong(chronoField);
        }
        else {
            return ldt.get(chronoField);
        }
    }

}
