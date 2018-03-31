package com.dioxic.mgenerate.operator.location;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;

@Operator
public class Country implements Resolvable<String> {

    @OperatorProperty
    Resolvable<Boolean> full = OperatorFactory.wrap(Boolean.FALSE);

	@Override
	public String resolve() {
        return full.resolve() ? FakerUtil.instance().address().countryCode(): FakerUtil.instance().address().country();

	}

}
