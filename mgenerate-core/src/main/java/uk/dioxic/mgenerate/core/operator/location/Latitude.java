package uk.dioxic.mgenerate.core.operator.location;

import uk.dioxic.mgenerate.common.State;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.util.List;

import static java.util.Arrays.asList;

@Operator
public class Latitude implements Resolvable<Double> {

    @OperatorProperty
    List<Number> bounds = asList(-90d, 90d);

	@Override
	public Double resolve() {
		return FakerUtil.randomDouble(bounds.get(0), bounds.get(1));
	}

}
