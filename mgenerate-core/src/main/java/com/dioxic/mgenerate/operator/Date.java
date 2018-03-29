package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import org.bson.types.MinKey;

@OperatorClass
public class Date implements Operator {

	private static final java.util.Date DEFAULT_MIN = new java.util.Date(0);
	private static final java.util.Date DEFAULT_MAX = new java.util.Date();

    @OperatorProperty
	Operator min = OperatorFactory.wrap(DEFAULT_MIN);

    @OperatorProperty
	Operator max = OperatorFactory.wrap(DEFAULT_MAX);

	@Override
	public MinKey resolve() {
		return FakerUtil.instance().date().between((java.util.Date) min.resolve(), (java.util.Date) max.resolve());
	}

}
