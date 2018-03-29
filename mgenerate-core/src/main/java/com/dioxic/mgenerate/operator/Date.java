package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;

@OperatorClass
public class Date implements Operator<java.util.Date> {

	private static final java.util.Date DEFAULT_MIN = new java.util.Date(0);
	private static final java.util.Date DEFAULT_MAX = new java.util.Date();

    @OperatorProperty
	Operator<java.util.Date> min = OperatorFactory.wrap(DEFAULT_MIN);

    @OperatorProperty
	Operator<java.util.Date> max = OperatorFactory.wrap(DEFAULT_MAX);

	@Override
	public java.util.Date resolve() {
		return FakerUtil.instance().date().between(min.resolve(), max.resolve());
	}

}
