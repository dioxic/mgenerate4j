package uk.dioxic.mgenerate.core.operator.chrono;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.time.LocalDateTime;

@Operator
public class Hour implements Resolvable<Integer> {

    @OperatorProperty
    Resolvable<LocalDateTime> date;

    @Override
    public Integer resolve() {
        return (date != null) ? date.resolve().getHour() : FakerUtil.numberBetween(0, 24);
    }

}
