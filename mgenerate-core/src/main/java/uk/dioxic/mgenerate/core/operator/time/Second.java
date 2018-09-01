package uk.dioxic.mgenerate.core.operator.time;

import uk.dioxic.mgenerate.core.util.FakerUtil;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Second implements Resolvable<Integer> {

    @Override
    public Integer resolve() {
        return FakerUtil.numberBetween(0, 59);
    }

}
