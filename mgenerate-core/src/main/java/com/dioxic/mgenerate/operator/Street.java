package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.annotation.OperatorClass;

@OperatorClass
public class Street implements Operator {

	@Override
	public String resolve() {
		return FakerUtil.instance().address().streetName();
	}

}
