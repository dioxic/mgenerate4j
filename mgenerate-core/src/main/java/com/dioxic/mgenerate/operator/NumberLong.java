package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class NumberLong implements Resolvable<Long> {

    @OperatorProperty
	Long min = Long.MIN_VALUE;

    @OperatorProperty
	Long max = Long.MAX_VALUE;

	@Override
	public Long resolve() {
		return FakerUtil.numberBetween(min, max);
	}
}
