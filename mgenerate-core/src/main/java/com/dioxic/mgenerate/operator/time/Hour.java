package com.dioxic.mgenerate.operator.time;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Hour implements Resolvable<Integer> {

    Resolvable<Boolean> twentyFour = OperatorFactory.wrap(Boolean.FALSE);

    @Override
    public Integer resolve() {
        return (twentyFour.resolve()) ? FakerUtil.numberBetween(1, 24) : FakerUtil.numberBetween(1, 12);
    }

}
