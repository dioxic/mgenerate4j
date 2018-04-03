package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Bool implements Resolvable<Boolean> {

	@Override
	public Boolean resolve() {
		return FakerUtil.randomBoolean();
	}

}
