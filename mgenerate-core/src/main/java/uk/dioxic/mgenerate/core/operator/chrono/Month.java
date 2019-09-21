package uk.dioxic.mgenerate.core.operator.chrono;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.type.DateDisplayType;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

@Operator
public class Month implements Resolvable<Object> {

    private static final String[] months = DateFormatSymbols.getInstance().getMonths();
    private static final String[] shortMonths = DateFormatSymbols.getInstance().getShortMonths();

    @OperatorProperty(primary = true)
    Resolvable<LocalDateTime> date;

    @OperatorProperty
    DateDisplayType type = DateDisplayType.NUMERIC;

    @OperatorProperty
    Locale locale = Locale.getDefault();

    @Override
    public Object resolve() {
        if (date != null) {
            java.time.Month month = date.resolve().getMonth();
            switch (type) {
                case LONG_TEXT:
                    return month.getDisplayName(TextStyle.FULL, locale);
                case SHORT_TEXT:
                    return month.getDisplayName(TextStyle.SHORT, locale);
                default:
                    return month.getValue();
            }
        }
        else {
            int month = FakerUtil.numberBetween(1, 8);
            switch (type) {
                case LONG_TEXT:
                    return months[month];
                case SHORT_TEXT:
                    return shortMonths[month];
                default:
                    return month;
            }
        }
    }

}
