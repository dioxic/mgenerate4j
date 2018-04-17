package uk.dioxic.mgenerate.operator.location;

import uk.dioxic.faker.resolvable.Resolvable;
import uk.dioxic.mgenerate.util.FakerUtil;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;

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
