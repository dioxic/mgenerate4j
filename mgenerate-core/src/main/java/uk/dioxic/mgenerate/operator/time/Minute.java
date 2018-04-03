package uk.dioxic.mgenerate.operator.time;

import uk.dioxic.mgenerate.FakerUtil;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Minute implements Resolvable<Integer> {

    @Override
    public Integer resolve() {
        return FakerUtil.numberBetween(0, 59);
    }

}
