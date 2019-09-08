package uk.dioxic.mgenerate.core.operator;

import uk.dioxic.mgenerate.common.Cache;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;

@Operator("objectid")
public class ObjectId implements Resolvable<org.bson.types.ObjectId> {

	@Override
	public org.bson.types.ObjectId resolve() {
		return org.bson.types.ObjectId.get();
	}

	@Override
	public org.bson.types.ObjectId resolve(Cache cache) {
		return resolve();
	}


}
