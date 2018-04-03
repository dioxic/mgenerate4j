package uk.dioxic.mgenerate.operator.person;

import uk.dioxic.mgenerate.FakerUtil;
import uk.dioxic.mgenerate.OperatorFactory;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;
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
