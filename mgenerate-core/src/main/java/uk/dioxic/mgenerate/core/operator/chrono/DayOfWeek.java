package uk.dioxic.mgenerate.core.operator.chrono;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.operator.type.DateDisplayType;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

@Operator
public class DayOfWeek extends AbstractOperator<Object> {

    private static final String[] weekDays = DateFormatSymbols.getInstance().getWeekdays();
    private static final String[] shortWeekDays = DateFormatSymbols.getInstance().getShortWeekdays();

    @OperatorProperty(primary = true)
    Resolvable<LocalDateTime> date;

    @OperatorProperty
    DateDisplayType type = DateDisplayType.NUMERIC;

    @OperatorProperty
    Locale locale = Locale.getDefault();

    @Override
    public Object resolveInternal() {
        if (date != null) {
            java.time.DayOfWeek dayOfWeek = date.resolve().getDayOfWeek();
            switch (type) {
                case LONG_TEXT:
                    return dayOfWeek.getDisplayName(TextStyle.FULL, locale);
                case SHORT_TEXT:
                    return dayOfWeek.getDisplayName(TextStyle.SHORT, locale);
                default:
                    return dayOfWeek.getValue();
            }
        }
        else {
            int dayOfWeek = FakerUtil.numberBetween(1, 8);
            switch (type) {
                case LONG_TEXT:
                    return weekDays[dayOfWeek];
                case SHORT_TEXT:
                    return shortWeekDays[dayOfWeek];
                default:
                    return dayOfWeek;
            }
        }
    }

}
