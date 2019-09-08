package uk.dioxic.mgenerate.core.operator.person;

import uk.dioxic.mgenerate.common.Cache;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator
public class Age implements Resolvable<Integer> {

    @OperatorProperty
    Resolvable<AgeType> type = Wrapper.wrap(AgeType.DEFAULT);

    @Override
    public Integer resolve() {
        return resolve(null);
    }

    @Override
    public Integer resolve(Cache cache) {
        AgeType typeValue = type.resolve(cache);
        return FakerUtil.numberBetween(typeValue.getMinAge(), typeValue.getMaxAge());
    }

}
