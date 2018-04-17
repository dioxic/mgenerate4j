package uk.dioxic.mgenerate.operator.time;

import uk.dioxic.mgenerate.util.FakerUtil;
import uk.dioxic.mgenerate.OperatorFactory;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Hour implements Resolvable<Integer> {

    Resolvable<Boolean> twentyFour = OperatorFactory.wrap(Boolean.FALSE);

    @Override
    public Integer resolve() {
        return (twentyFour.resolve()) ? FakerUtil.numberBetween(1, 24) : FakerUtil.numberBetween(1, 12);
    }

}
