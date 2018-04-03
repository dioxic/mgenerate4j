package com.dioxic.mgenerate.operator.time;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Minute implements Resolvable<Integer> {

    @Override
    public Integer resolve() {
        return FakerUtil.numberBetween(0, 59);
    }

}
