package uk.dioxic.mgenerate.core.operator.sequence;

import uk.dioxic.mgenerate.common.Initializable;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;

import java.util.concurrent.atomic.AtomicLong;

@Operator({"longSeq", "intSequence"})
public class LongSequence implements Resolvable<Long>, Initializable {

    @OperatorProperty(primary = true)
    Resolvable<Long> step = Wrapper.wrap(1L);

    @OperatorProperty
    Long start = 0L;

    private AtomicLong counter;

    @Override
    public Long resolve() {
        return counter.getAndUpdate(n -> n + step.resolve());
    }

    @Override
    public void initialize() {
        counter = new AtomicLong(start);
    }
    
}
