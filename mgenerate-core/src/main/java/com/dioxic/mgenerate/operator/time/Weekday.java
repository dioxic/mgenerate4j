package com.dioxic.mgenerate.operator.time;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;

import java.text.DateFormatSymbols;

@Operator
public class Weekday implements Resolvable<String> {

    private static final String[] weekDays = DateFormatSymbols.getInstance().getWeekdays();
    private static final String[] shortWeekDays = DateFormatSymbols.getInstance().getShortWeekdays();

    @OperatorProperty
    Resolvable<Boolean> weekday_only = OperatorFactory.wrap(Boolean.FALSE);

    @Override
    public String resolve() {
        int day = FakerUtil.numberBetween(1,7);

        return weekday_only.resolve() ? shortWeekDays[day] : weekDays[day];
    }

}
