package uk.dioxic.mgenerate.operator.time;

import uk.dioxic.mgenerate.FakerUtil;
import uk.dioxic.mgenerate.OperatorFactory;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;
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
