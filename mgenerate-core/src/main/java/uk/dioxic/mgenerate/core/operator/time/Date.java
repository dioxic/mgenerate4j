package uk.dioxic.mgenerate.core.operator.time;

import uk.dioxic.mgenerate.common.State;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.time.LocalDateTime;

@Operator({"dateTime", "ts"})
public class Date implements Resolvable<LocalDateTime> {
    // TODO ISO-8601
	private static final LocalDateTime DEFAULT_MIN = LocalDateTime.parse("1900-01-01T00:00:00");
	private static final LocalDateTime DEFAULT_MAX = LocalDateTime.parse("2099-12-31T23:59:59");

    @OperatorProperty
    Resolvable<LocalDateTime> min = Wrapper.wrap(DEFAULT_MIN);

    @OperatorProperty
    Resolvable<LocalDateTime> max = Wrapper.wrap(DEFAULT_MAX);

	@Override
	public LocalDateTime resolve() {
		return FakerUtil.randomDate(min.resolve(), max.resolve());
	}


}
