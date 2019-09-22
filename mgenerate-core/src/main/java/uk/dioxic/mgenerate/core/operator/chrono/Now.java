package uk.dioxic.mgenerate.core.operator.chrono;

import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;

import java.time.LocalDateTime;

@Operator
public class Now extends AbstractOperator<LocalDateTime> {

	@Override
	public LocalDateTime resolveInternal() {
		return LocalDateTime.now();
	}

}
