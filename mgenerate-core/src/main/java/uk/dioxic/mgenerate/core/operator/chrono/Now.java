package uk.dioxic.mgenerate.core.operator.chrono;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;

import java.time.LocalDateTime;

@Operator
public class Now implements Resolvable<LocalDateTime> {

	@Override
	public LocalDateTime resolve() {
		return LocalDateTime.now();
	}

}
