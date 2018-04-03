package com.dioxic.mgenerate.operator.location;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class City implements Resolvable<String> {

	@Override
	public String resolve() {
		return FakerUtil.getFake("address.city");
	}

}
