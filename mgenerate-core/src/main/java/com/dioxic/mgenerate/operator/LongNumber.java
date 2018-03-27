package com.dioxic.mgenerate.operator;

import java.util.Optional;

import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import org.bson.Document;

import com.dioxic.mgenerate.FakerUtil;

@OperatorClass
public class LongNumber implements Operator {

	private static final long DEFAULT_MIN = Long.MIN_VALUE;
	private static final long DEFAULT_MAX = Long.MAX_VALUE;

    @OperatorProperty
	Operator min = OperatorFactory.wrap(DEFAULT_MIN);

    @OperatorProperty
	Operator max = OperatorFactory.wrap(DEFAULT_MAX);

	@Override
	public Long resolve() {
		return FakerUtil.instance().number().numberBetween((Long) min.resolve(), (Long) max.resolve());
	}
}
