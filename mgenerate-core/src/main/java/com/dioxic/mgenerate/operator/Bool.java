package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.OperatorClass;

@OperatorClass
public class Bool implements Resolvable<Boolean> {

	@Override
	public Boolean resolve() {
		return FakerUtil.randomBoolean();
	}

}
