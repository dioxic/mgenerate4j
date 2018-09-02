package uk.dioxic.mgenerate.core.operator.time;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator
public class Hour implements Resolvable<Integer> {

    Resolvable<Boolean> twentyFour = Wrapper.wrap(Boolean.FALSE);

    @Override
    public Integer resolve() {
        return (twentyFour.resolve()) ? FakerUtil.numberBetween(1, 24) : FakerUtil.numberBetween(1, 12);
    }

}
