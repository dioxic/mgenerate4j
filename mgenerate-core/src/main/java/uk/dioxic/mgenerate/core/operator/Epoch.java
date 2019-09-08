package uk.dioxic.mgenerate.core.operator;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Cache;
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
        return resolve(null);
    }

    @Override
    public Number resolve(Cache cache) {
        return format(input.resolve(cache));
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
