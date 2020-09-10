package uk.dioxic.mgenerate.core.operator.sequence;

import uk.dioxic.mgenerate.common.Initializable;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;

import java.util.concurrent.atomic.AtomicLong;

@Operator({"longSeq", "longSequence"})
public class LongSequence extends AbstractOperator<Long> implements Initializable {

    @OperatorProperty(primary = true)
    Resolvable<Long> step = Wrapper.wrap(1L);

    @OperatorProperty
    Long start = 0L;

    private AtomicLong counter;

    @Override
    public Long resolveInternal() {
        return counter.getAndUpdate(n -> n + step.resolve());
    }

    @Override
    public void initialize() {
        counter = new AtomicLong(start);
    }
    
}
