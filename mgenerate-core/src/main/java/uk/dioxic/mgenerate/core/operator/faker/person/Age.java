package uk.dioxic.mgenerate.core.operator.faker.person;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.operator.type.AgeType;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator
public class Age extends AbstractOperator<Integer> {

    @OperatorProperty
    Resolvable<AgeType> type = Wrapper.wrap(AgeType.DEFAULT);

    @Override
    public Integer resolveInternal() {
        AgeType typeValue = type.resolve();
        return FakerUtil.numberBetween(typeValue.getMinAge(), typeValue.getMaxAge());
    }

}
