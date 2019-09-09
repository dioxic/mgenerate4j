package uk.dioxic.mgenerate.core.operator.time;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.text.DateFormatSymbols;

@Operator
public class Month implements Resolvable<String> {

    private static final String[] months = DateFormatSymbols.getInstance().getMonths();
    private static final String[] shortMonths = DateFormatSymbols.getInstance().getShortMonths();

    @OperatorProperty
    Resolvable<Boolean> full = Wrapper.wrap(Boolean.TRUE);

    @Override
    public String resolve() {
        int month = FakerUtil.numberBetween(0,11);

        return full.resolve() ? months[month] : shortMonths[month];
    }

}
