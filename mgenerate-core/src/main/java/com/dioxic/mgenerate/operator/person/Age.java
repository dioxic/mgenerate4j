package com.dioxic.mgenerate.operator.person;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import com.dioxic.mgenerate.Resolvable;

@OperatorClass
public class Age implements Resolvable<Integer> {

    @OperatorProperty
    Resolvable<AgeType> type = OperatorFactory.wrap(AgeType.DEFAULT);

    @Override
    public Integer resolve() {
        AgeType typeValue = type.resolve();
        return FakerUtil.numberBetween(typeValue.getMinAge(), typeValue.getMaxAge());
    }

}
