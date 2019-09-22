package uk.dioxic.mgenerate.core.operator.chrono;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Operator
public class Epoch extends AbstractOperator<Number> {

    private static final LocalDateTime epoch = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);

    @OperatorProperty
    Resolvable<LocalDateTime> date = Wrapper.wrap(LocalDateTime.now());

    @OperatorProperty
    ChronoUnit unit = ChronoUnit.MILLIS;

    @Override
    public Number resolveInternal() {
        return unit.between(epoch, date.resolve());
    }

}
