package uk.dioxic.mgenerate.core.operator.time;

import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

import java.util.Date;

@Operator
public class Now implements Resolvable<Date> {

	@Override
	public Date resolve() {
		return new Date();
	}

}
