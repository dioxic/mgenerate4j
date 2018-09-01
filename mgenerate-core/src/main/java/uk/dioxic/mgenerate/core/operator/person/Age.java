package uk.dioxic.mgenerate.core.operator.person;

import uk.dioxic.mgenerate.core.util.FakerUtil;
import uk.dioxic.mgenerate.common.OperatorFactory;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Age implements Resolvable<Integer> {

    @OperatorProperty
    Resolvable<AgeType> type = OperatorFactory.wrap(AgeType.DEFAULT);

    @Override
    public Integer resolve() {
        AgeType typeValue = type.resolve();
        return FakerUtil.numberBetween(typeValue.getMinAge(), typeValue.getMaxAge());
    }

}
