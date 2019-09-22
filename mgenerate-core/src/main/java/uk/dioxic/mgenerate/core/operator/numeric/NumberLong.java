package uk.dioxic.mgenerate.core.operator.numeric;

import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator({"mgNumberLong", "long", "int64"})
public class NumberLong extends AbstractOperator<Long> {

    @OperatorProperty
	Long min = Long.MIN_VALUE;

    @OperatorProperty
	Long max = Long.MAX_VALUE;

	@Override
	public Long resolveInternal() {
		return FakerUtil.numberBetween(min, max);
	}

}
