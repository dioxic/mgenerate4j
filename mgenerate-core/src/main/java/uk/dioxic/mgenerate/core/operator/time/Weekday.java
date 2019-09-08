package uk.dioxic.mgenerate.core.operator.time;

import uk.dioxic.mgenerate.common.Cache;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.text.DateFormatSymbols;

@Operator
public class Weekday implements Resolvable<String> {

    private static final String[] weekDays = DateFormatSymbols.getInstance().getWeekdays();
    private static final String[] shortWeekDays = DateFormatSymbols.getInstance().getShortWeekdays();

    @OperatorProperty
    Resolvable<Boolean> weekday_only = Wrapper.wrap(Boolean.FALSE);

    @Override
    public String resolve() {
        return resolve(null);
    }

    @Override
    public String resolve(Cache cache) {
        int day = FakerUtil.numberBetween(1, 7);

        return weekday_only.resolve(cache) ? shortWeekDays[day] : weekDays[day];
    }

}
