package uk.dioxic.mgenerate.core.operator.core;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;

@Operator("objectid")
public class ObjectId implements Resolvable<org.bson.types.ObjectId> {

	@Override
	public org.bson.types.ObjectId resolve() {
		return org.bson.types.ObjectId.get();
	}

}
