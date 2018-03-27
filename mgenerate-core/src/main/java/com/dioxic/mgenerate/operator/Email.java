package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import org.bson.Document;

import com.dioxic.mgenerate.FakerUtil;

@OperatorClass
public class Email implements Operator {

    @OperatorProperty
	Operator domain;

	@Override
	public String resolve() {
		return domain != null ? FakerUtil.instance().internet().emailAddress(domain.resolve().toString()) : FakerUtil.instance().internet().emailAddress();
	}

}
