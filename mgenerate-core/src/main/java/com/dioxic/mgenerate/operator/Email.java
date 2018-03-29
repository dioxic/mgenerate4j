package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import org.bson.Document;

@OperatorClass
public class Email implements Operator<String> {

    @OperatorProperty
	Operator<String> domain;

    @Override
	public String resolve() {
		return domain != null ? FakerUtil.instance().internet().emailAddress(domain.resolve())
                : FakerUtil.instance().internet().emailAddress();
	}

}
