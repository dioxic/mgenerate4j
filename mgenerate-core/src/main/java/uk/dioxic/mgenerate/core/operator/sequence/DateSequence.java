package uk.dioxic.mgenerate.core.operator.sequence;

import uk.dioxic.mgenerate.common.Initializable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicLong;

@Operator({"dateSeq","dateSequence"})
public class DateSequence extends AbstractOperator<LocalDateTime> implements Initializable {

    @OperatorProperty(primary = true)
    Long step = 1L;

    @OperatorProperty
    ChronoUnit chronoUnit = ChronoUnit.SECONDS;

    @OperatorProperty
    LocalDateTime start = LocalDateTime.now();

    private AtomicLong counter;

    @Override
    public LocalDateTime resolveInternal() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(counter.getAndUpdate(n -> n + step)), ZoneOffset.UTC);
    }

    @Override
    public void initialize() {
        counter = new AtomicLong(start.toInstant(ZoneOffset.UTC).toEpochMilli());
        step = toMillis(chronoUnit, step);
    }

    /**
     * convert chronoUnit step into millis epoch representation
     *
     * @return
     */
    private Long toMillis(ChronoUnit chronoUnit, Long step) {
        LocalDateTime dt = LocalDateTime.now();
        LocalDateTime dtStep = dt.plus(step, chronoUnit);

        return dtStep.toInstant(ZoneOffset.UTC).toEpochMilli() - dt.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
