package uk.dioxic.mgenerate.core.operator.time;

import uk.dioxic.mgenerate.common.Cache;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.util.Date;

@Operator
public class Second implements Resolvable<Integer> {

    @Override
    public Integer resolve() {
        return FakerUtil.numberBetween(0, 59);
    }

    @Override
    public Integer resolve(Cache cache) {
        return resolve();
    }

}
