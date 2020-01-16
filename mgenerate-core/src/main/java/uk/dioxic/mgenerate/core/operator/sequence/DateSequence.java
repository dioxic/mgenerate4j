package uk.dioxic.mgenerate.core.operator.sequence;

import uk.dioxic.mgenerate.common.Initializable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongUnaryOperator;

import static java.time.temporal.ChronoUnit.MILLIS;

@Operator({"dateSeq","dateSequence"})
public class DateSequence extends AbstractOperator<LocalDateTime> implements Initializable {

    @OperatorProperty(primary = true)
    Long step = 1L;

    @OperatorProperty
    ChronoUnit chronoUnit = ChronoUnit.SECONDS;

    @OperatorProperty
    LocalDateTime start = LocalDateTime.now();

    private AtomicLong counter;
    private LongUnaryOperator inc;

    @Override
    public LocalDateTime resolveInternal() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(counter.getAndUpdate(inc)), ZoneOffset.UTC);
    }

    @Override
    public void initialize() {
        counter = new AtomicLong(start.toInstant(ZoneOffset.UTC).toEpochMilli());

        List<ChronoUnit> simpleInc = Arrays.asList(ChronoUnit.NANOS,MILLIS,ChronoUnit.SECONDS,ChronoUnit.MINUTES,ChronoUnit.HOURS,ChronoUnit.DAYS,ChronoUnit.WEEKS);

        if (simpleInc.contains(chronoUnit)) {
            step = convertToMillis(chronoUnit, step);
            chronoUnit = MILLIS;
            inc = millisInc;
        }
        else {
            inc = dtInc;
        }
    }

    /**
     * convert chronoUnit step into millis epoch representation
     *
     * @return
     */
    private Long convertToMillis(ChronoUnit chronoUnit, Long step) {
        LocalDateTime dt = LocalDateTime.now();
        LocalDateTime dtStep = dt.plus(step, chronoUnit);

        return dtStep.toInstant(ZoneOffset.UTC).toEpochMilli() - dt.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    private LocalDateTime toLdt(long millis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC);
    }

    private long toMillis(LocalDateTime ldt) {
        return ldt.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    private LongUnaryOperator dtInc = n -> toMillis(toLdt(n).plus(step, chronoUnit));

    private LongUnaryOperator millisInc = n -> n + step;

}
