package com.dioxic.mgenerate.operator.location;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.Operator;

@Operator
public class Zip implements Resolvable<String> {

	@Override
	public String resolve() {
		return FakerUtil.instance().address().zipCode();
	}

}
