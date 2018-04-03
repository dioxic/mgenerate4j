package uk.dioxic.mgenerate.operator;

import uk.dioxic.mgenerate.Initializable;
import uk.dioxic.mgenerate.OperatorFactory;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

import java.util.concurrent.atomic.AtomicInteger;

@Operator
public class Inc implements Resolvable<Integer>, Initializable {

    @OperatorProperty
    Resolvable<Integer> step = OperatorFactory.wrap(1);

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
