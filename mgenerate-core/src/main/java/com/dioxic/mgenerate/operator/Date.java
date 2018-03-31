package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Operator
public class Date implements Resolvable<java.util.Date> {
    // TODO ISO-8601
	private static final LocalDateTime DEFAULT_MIN = LocalDateTime.parse("1900-01-01T00:00:00");
	private static final LocalDateTime DEFAULT_MAX = LocalDateTime.parse("2099-12-31T23:59:59");

    @OperatorProperty
    Resolvable<LocalDateTime> min = OperatorFactory.wrap(DEFAULT_MIN);

    @OperatorProperty
    Resolvable<LocalDateTime> max = OperatorFactory.wrap(DEFAULT_MAX);

	@Override
	public java.util.Date resolve() {
		return java.util.Date.from(FakerUtil.randomDate(min.resolve(), max.resolve()).toInstant(ZoneOffset.UTC));
	}

}
