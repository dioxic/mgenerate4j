package uk.dioxic.mgenerate.operator.location;

import uk.dioxic.mgenerate.FakerUtil;
import uk.dioxic.mgenerate.OperatorFactory;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Country implements Resolvable<String> {

    @OperatorProperty
    Resolvable<Boolean> full = OperatorFactory.wrap(Boolean.FALSE);

	@Override
	public String resolve() {
        return full.resolve() ? FakerUtil.getFake("address.country_code"): FakerUtil.getFake("address.country");

	}

}
