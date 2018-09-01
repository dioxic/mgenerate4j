package uk.dioxic.mgenerate.core.operator.person;

import uk.dioxic.mgenerate.core.util.FakerUtil;
import uk.dioxic.mgenerate.common.OperatorFactory;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

import java.time.ZoneOffset;
import java.util.Date;

@Operator
public class Birthday implements Resolvable<Object> {

    @OperatorProperty
    Resolvable<AgeType> type = OperatorFactory.wrap(AgeType.DEFAULT);

    @OperatorProperty
    Resolvable<Boolean> string = OperatorFactory.wrap(Boolean.FALSE);

    @Override
    public Object resolve() {
        AgeType ageType = type.resolve();
        Date date = Date.from(FakerUtil.randomDate(ageType.getMinBirthday(), ageType.getMaxBirthday()).toInstant(ZoneOffset.UTC));

        return date;
    }

}
