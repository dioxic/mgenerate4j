package uk.dioxic.mgenerate.core.operator.general;

import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;

@Operator
public class ObjectId extends AbstractOperator<org.bson.types.ObjectId> {

	@Override
	public org.bson.types.ObjectId resolveInternal() {
		return org.bson.types.ObjectId.get();
	}

}
