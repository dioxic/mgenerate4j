package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.annotation.OperatorClass;

@OperatorClass("objectid")
public class ObjectId implements Operator<org.bson.types.ObjectId> {

	@Override
	public org.bson.types.ObjectId resolve() {
		return org.bson.types.ObjectId.get();
	}
}
