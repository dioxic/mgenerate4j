package uk.dioxic.mgenerate.operator.location;

import uk.dioxic.mgenerate.FakerUtil;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Latitude implements Resolvable<Double> {

	@Override
	public Double resolve() {
		return FakerUtil.randomDouble(-90,90);
	}

}
