package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.annotation.OperatorClass;
import org.bson.types.MinKey;

@OperatorClass("objectid")
public class ObjectId implements Operator {

	@Override
	public MinKey resolve() {
		return org.bson.types.ObjectId.get();
	}
}
