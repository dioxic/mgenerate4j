package uk.dioxic.mgenerate.core.operator.numeric;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator({"mgNumberLong", "long", "int64"})
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
