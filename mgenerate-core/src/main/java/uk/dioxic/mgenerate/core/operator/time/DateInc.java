package uk.dioxic.mgenerate.core.operator.time;

import uk.dioxic.mgenerate.common.Initializable;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.common.exception.TerminateGenerationException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicLong;

@Operator
public class DateInc implements Resolvable<LocalDateTime>, Initializable {

    @OperatorProperty
    Long step = 1L;

    @OperatorProperty
    ChronoUnit chronoUnit = ChronoUnit.SECONDS;

    @OperatorProperty
    LocalDateTime start = LocalDateTime.now();

    @OperatorProperty
    LocalDateTime end;

    private AtomicLong counter;
    private Long epochEnd;

    @Override
    public LocalDateTime resolve() {
        if (epochEnd != null && counter.get() > epochEnd) {
            throw new TerminateGenerationException();
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(counter.getAndUpdate(n -> n + step)), ZoneOffset.UTC);
    }

    @Override
    public void initialize() {
        counter = new AtomicLong(start.toInstant(ZoneOffset.UTC).toEpochMilli());
        step = calculateStep(chronoUnit, step);
        if (end != null) {
            epochEnd = end.toInstant(ZoneOffset.UTC).toEpochMilli();
        }
    }

    /**
     * convert chronoUnit step into millis epoch representation
     *
     * @return
     */
    private Long calculateStep(ChronoUnit chronoUnit, Long step) {
        LocalDateTime dt = LocalDateTime.now();
        LocalDateTime dtStep = dt.plus(step, chronoUnit);

        return dtStep.toInstant(ZoneOffset.UTC).toEpochMilli() - dt.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
