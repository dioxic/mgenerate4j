package com.dioxic.mgenerate.operator.location;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Street implements Resolvable<String> {

	@Override
	public String resolve() {
		return FakerUtil.getFake("address.street_name");
	}

}
