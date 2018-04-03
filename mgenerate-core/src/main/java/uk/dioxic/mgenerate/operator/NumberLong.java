package uk.dioxic.mgenerate.operator;

import uk.dioxic.mgenerate.FakerUtil;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;
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
