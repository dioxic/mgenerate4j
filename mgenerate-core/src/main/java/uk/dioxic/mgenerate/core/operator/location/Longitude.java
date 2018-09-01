package uk.dioxic.mgenerate.core.operator.location;

import uk.dioxic.mgenerate.core.util.FakerUtil;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Longitude implements Resolvable<Double> {

	@Override
	public Double resolve() {
		return FakerUtil.randomDouble(-180,180);
	}

}
