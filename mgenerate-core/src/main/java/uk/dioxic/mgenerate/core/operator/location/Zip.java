package uk.dioxic.mgenerate.core.operator.location;

import uk.dioxic.mgenerate.core.util.FakerUtil;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Zip implements Resolvable<String> {

	@Override
	public String resolve() {
		return FakerUtil.getValue("address.postcode");
	}

}
