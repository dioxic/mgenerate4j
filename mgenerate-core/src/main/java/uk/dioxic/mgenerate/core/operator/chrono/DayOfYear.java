package uk.dioxic.mgenerate.core.operator.chrono;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.time.LocalDateTime;

@Operator
public class DayOfYear implements Resolvable<Integer> {

    @OperatorProperty
    Resolvable<LocalDateTime> date;

    @Override
    public Integer resolve() {
        if (date != null) {
            return date.resolve().getDayOfYear();
        }

        return FakerUtil.numberBetween(1, 365+1);
    }

}
