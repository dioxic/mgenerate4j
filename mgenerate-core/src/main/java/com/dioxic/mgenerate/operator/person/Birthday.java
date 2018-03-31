package com.dioxic.mgenerate.operator.person;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import com.dioxic.mgenerate.Resolvable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Operator
public class Birthday implements Resolvable<Object> {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ISO_DATE;

    @OperatorProperty
    Resolvable<AgeType> type = OperatorFactory.wrap(AgeType.DEFAULT);

    @OperatorProperty
    Resolvable<Boolean> string = OperatorFactory.wrap(Boolean.FALSE);

    @Override
    public Object resolve() {
        AgeType ageType = type.resolve();
        Date date = Date.from(FakerUtil.randomDate(ageType.getMinBirthday(), ageType.getMaxBirthday()).toInstant(ZoneOffset.UTC));

//        if (string.resolve()) {
//            return date.format(DTF);
//        }

        return date;
    }

}
