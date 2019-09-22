package uk.dioxic.mgenerate.core.operator.chrono;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.time.LocalDateTime;

@Operator({"dateTime", "dt", "mgDate"})
public class Date extends AbstractOperator<LocalDateTime> {
    // TODO ISO-8601
	static final LocalDateTime DEFAULT_MIN = LocalDateTime.parse("1990-01-01T00:00:00");
	static final LocalDateTime DEFAULT_MAX = LocalDateTime.parse("2020-01-01T00:00:00");

    @OperatorProperty
    Resolvable<LocalDateTime> min = Wrapper.wrap(DEFAULT_MIN);

    @OperatorProperty
    Resolvable<LocalDateTime> max = Wrapper.wrap(DEFAULT_MAX);

	@Override
	public LocalDateTime resolveInternal() {
		return FakerUtil.randomDate(min.resolve(), max.resolve());
	}


}
