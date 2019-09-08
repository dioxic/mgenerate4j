package uk.dioxic.mgenerate.core.operator.location;

import uk.dioxic.mgenerate.common.State;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator
public class Postal implements Resolvable<String> {

	@Override
	public String resolve() {
		return FakerUtil.getValue("address.postcode");
	}

}
