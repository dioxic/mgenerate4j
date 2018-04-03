package com.dioxic.mgenerate.operator.location;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Country implements Resolvable<String> {

    @OperatorProperty
    Resolvable<Boolean> full = OperatorFactory.wrap(Boolean.FALSE);

	@Override
	public String resolve() {
        return full.resolve() ? FakerUtil.getFake("address.country_code"): FakerUtil.getFake("address.country");

	}

}
