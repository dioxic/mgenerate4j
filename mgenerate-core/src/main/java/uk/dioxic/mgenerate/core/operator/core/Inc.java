package uk.dioxic.mgenerate.core.operator;

import uk.dioxic.mgenerate.common.State;
import uk.dioxic.mgenerate.common.Initializable;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;

import java.util.concurrent.atomic.AtomicInteger;

@Operator
public class Inc implements Resolvable<Integer>, Initializable {

    @OperatorProperty
    Resolvable<Integer> step = Wrapper.wrap(1);

    @OperatorProperty
    Integer start = 0;

    @OperatorProperty
    Boolean threadLocal = false;

    private AtomicInteger counter;
    private ThreadLocal<Integer> localCounter;

    @Override
    public Integer resolve() {
        if (threadLocal) {
            Integer current = localCounter.get();
            localCounter.set(current + step.resolve());
            return current;
        }
        else {
            return counter.getAndUpdate(n -> n + step.resolve());
        }
    }

    @Override
    public void initialize() {
        if (threadLocal) {
            localCounter = ThreadLocal.withInitial(() -> start);
        }
        else {
            counter = new AtomicInteger(start);
        }
    }
}
