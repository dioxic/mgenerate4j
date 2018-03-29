package com.dioxic.mgenerate.operator.person;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import com.dioxic.mgenerate.operator.Operator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@OperatorClass
public class Birthday implements Operator<Object> {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ISO_DATE;

    @OperatorProperty
    Operator<AgeType> type = OperatorFactory.wrap(AgeType.DEFAULT);

    @OperatorProperty
    Operator<Boolean> string = OperatorFactory.wrap(Boolean.FALSE);

    @Override
    public Object resolve() {
        AgeType ageType = type.resolve();
        LocalDateTime date = FakerUtil.randomDate(ageType.getMinBirthday(), ageType.getMaxBirthday());

        if (string.resolve()) {
            return date.format(DTF);
        }

        return date;
    }

}
