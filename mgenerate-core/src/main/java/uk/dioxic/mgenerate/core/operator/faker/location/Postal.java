package uk.dioxic.mgenerate.core.operator.faker.location;

import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator
public class Postal extends AbstractOperator<String> {

	@Override
	public String resolveInternal() {
		return FakerUtil.getValue("address.postcode");
	}

}
