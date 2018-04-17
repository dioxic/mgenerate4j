package uk.dioxic.mgenerate.operator.location;

import uk.dioxic.mgenerate.FakerUtil;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Longitude implements Resolvable<Double> {

	@Override
	public Double resolve() {
		return FakerUtil.randomDouble(-180,180);
	}

}
