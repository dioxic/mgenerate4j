package uk.dioxic.mgenerate.core.operator.person;

import uk.dioxic.mgenerate.common.State;
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
        AgeType typeValue = type.resolve();
        return FakerUtil.numberBetween(typeValue.getMinAge(), typeValue.getMaxAge());
    }

}
