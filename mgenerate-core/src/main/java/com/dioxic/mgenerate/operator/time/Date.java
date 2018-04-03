package com.dioxic.mgenerate.operator.time;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

import java.time.LocalDateTime;

@Operator
public class Date implements Resolvable<LocalDateTime> {
    // TODO ISO-8601
	private static final LocalDateTime DEFAULT_MIN = LocalDateTime.parse("1900-01-01T00:00:00");
	private static final LocalDateTime DEFAULT_MAX = LocalDateTime.parse("2099-12-31T23:59:59");

    @OperatorProperty
    Resolvable<LocalDateTime> min = OperatorFactory.wrap(DEFAULT_MIN);

    @OperatorProperty
    Resolvable<LocalDateTime> max = OperatorFactory.wrap(DEFAULT_MAX);

	@Override
	public LocalDateTime resolve() {
		return FakerUtil.randomDate(min.resolve(), max.resolve());
	}

}
