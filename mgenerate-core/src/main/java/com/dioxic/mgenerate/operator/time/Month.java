package com.dioxic.mgenerate.operator.time;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

import java.text.DateFormatSymbols;

@Operator
public class Month implements Resolvable<String> {

    private static final String[] months = DateFormatSymbols.getInstance().getMonths();
    private static final String[] shortMonths = DateFormatSymbols.getInstance().getShortMonths();

    @OperatorProperty
    Resolvable<Boolean> full = OperatorFactory.wrap(Boolean.TRUE);

    @Override
    public String resolve() {
        int month = FakerUtil.numberBetween(0,11);

        return full.resolve() ? months[month] : shortMonths[month];
    }

}
