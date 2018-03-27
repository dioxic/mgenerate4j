package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.annotation.OperatorClass;

@OperatorClass
public class ObjectId implements Operator {

	@Override
	public org.bson.types.ObjectId resolve() {
		return org.bson.types.ObjectId.get();
	}
}
