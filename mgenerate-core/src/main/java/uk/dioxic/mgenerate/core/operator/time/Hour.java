package uk.dioxic.mgenerate.core.operator.time;

import uk.dioxic.mgenerate.core.util.FakerUtil;
import uk.dioxic.mgenerate.common.OperatorFactory;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Hour implements Resolvable<Integer> {

    Resolvable<Boolean> twentyFour = OperatorFactory.wrap(Boolean.FALSE);

    @Override
    public Integer resolve() {
        return (twentyFour.resolve()) ? FakerUtil.numberBetween(1, 24) : FakerUtil.numberBetween(1, 12);
    }

}
