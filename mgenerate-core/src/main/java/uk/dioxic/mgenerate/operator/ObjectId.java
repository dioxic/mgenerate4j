package uk.dioxic.mgenerate.operator;

import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator("objectid")
public class ObjectId implements Resolvable<org.bson.types.ObjectId> {

	@Override
	public org.bson.types.ObjectId resolve() {
		return org.bson.types.ObjectId.get();
	}
}
