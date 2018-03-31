package com.dioxic.mgenerate.operator.time;

import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.Operator;

import java.util.Date;

@Operator
public class Now implements Resolvable<Date> {

	@Override
	public Date resolve() {
		return new Date();
	}

}
