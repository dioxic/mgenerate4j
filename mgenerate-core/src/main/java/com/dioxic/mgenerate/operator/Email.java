package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import org.bson.Document;

import com.dioxic.mgenerate.FakerUtil;
import org.bson.types.MinKey;

@OperatorClass
public class Email implements Operator {

    @OperatorProperty
	private Operator domain;

    public Email() {}

    public Email(Document document) {
        domain = document.get("domain", Operator.class);
    }

    @Override
	public MinKey resolve() {
		return domain != null ? FakerUtil.instance().internet().emailAddress(domain.resolve().toString())
                : FakerUtil.instance().internet().emailAddress();
	}

}
