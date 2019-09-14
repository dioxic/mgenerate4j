package uk.dioxic.mgenerate.core.operator.sequence;

import uk.dioxic.mgenerate.common.Initializable;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.common.exception.TerminateGenerationException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Operator({"seq", "intSeq", "sequence", "intSequence"})
public class IntSequence implements Resolvable<Integer>, Initializable {

    @OperatorProperty(primary = true)
    Resolvable<Integer> step = Wrapper.wrap(1);

    @OperatorProperty
    Integer start = 0;

    private AtomicInteger counter;

    @Override
    public Integer resolve() {
        return counter.getAndUpdate(n -> n + step.resolve());
    }

    @Override
    public void initialize() {
        counter = new AtomicInteger(start);
    }

}
