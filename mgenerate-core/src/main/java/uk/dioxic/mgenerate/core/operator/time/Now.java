package uk.dioxic.mgenerate.core.operator.time;

import uk.dioxic.mgenerate.common.Cache;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;

import java.util.Date;

@Operator
public class Now implements Resolvable<Date> {

	@Override
	public Date resolve() {
		return new Date();
	}

	@Override
	public Date resolve(Cache cache) {
		return resolve();
	}

}
