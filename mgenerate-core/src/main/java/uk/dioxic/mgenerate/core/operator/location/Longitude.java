package uk.dioxic.mgenerate.core.operator.location;

import uk.dioxic.mgenerate.common.State;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator
public class Longitude implements Resolvable<Double> {

	@Override
	public Double resolve() {
		return FakerUtil.randomDouble(-180,180);
	}

}
