package uk.dioxic.mgenerate.core.operator.sequence;

import uk.dioxic.mgenerate.common.Initializable;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;

import java.util.concurrent.atomic.AtomicInteger;

@Operator({"seq", "intSeq", "sequence", "intSequence"})
public class IntSequence extends AbstractOperator<Integer> implements Initializable {

    @OperatorProperty(primary = true)
    Resolvable<Integer> step = Wrapper.wrap(1);

    @OperatorProperty
    Integer start = 0;

    private AtomicInteger counter;

    @Override
    public Integer resolveInternal() {
        return counter.getAndUpdate(n -> n + step.resolve());
    }

    @Override
    public void initialize() {
        counter = new AtomicInteger(start);
    }

}
