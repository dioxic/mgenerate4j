package uk.dioxic.mgenerate.operator.location;

import uk.dioxic.mgenerate.FakerUtil;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Postal implements Resolvable<String> {

	@Override
	public String resolve() {
		return FakerUtil.getValue("address.postcode");
	}

}
