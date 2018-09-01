package uk.dioxic.mgenerate.core.operator.time;

import uk.dioxic.mgenerate.core.util.FakerUtil;
import uk.dioxic.mgenerate.common.OperatorFactory;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
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
