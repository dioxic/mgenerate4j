package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;

@OperatorClass
public class Email implements Resolvable<String> {

    @OperatorProperty
    Resolvable<String> domain;

    @Override
	public String resolve() {
		return domain != null ? FakerUtil.instance().internet().emailAddress(domain.resolve())
                : FakerUtil.instance().internet().emailAddress();
	}

}
