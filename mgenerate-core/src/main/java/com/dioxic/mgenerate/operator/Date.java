package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;

import java.time.LocalDateTime;

@OperatorClass
public class Date implements Resolvable<LocalDateTime> {

	private static final LocalDateTime DEFAULT_MIN = LocalDateTime.MIN;
	private static final LocalDateTime DEFAULT_MAX = LocalDateTime.MAX;

    @OperatorProperty
    Resolvable<LocalDateTime> min = OperatorFactory.wrap(DEFAULT_MIN);

    @OperatorProperty
    Resolvable<LocalDateTime> max = OperatorFactory.wrap(DEFAULT_MAX);

	@Override
	public LocalDateTime resolve() {
		return FakerUtil.randomDate(min.resolve(), max.resolve());
	}

}
