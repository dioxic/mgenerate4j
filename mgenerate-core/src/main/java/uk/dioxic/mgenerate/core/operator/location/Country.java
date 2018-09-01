package uk.dioxic.mgenerate.core.operator.location;

import uk.dioxic.mgenerate.core.util.FakerUtil;
import uk.dioxic.mgenerate.common.OperatorFactory;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Country implements Resolvable<String> {

    @OperatorProperty
    Resolvable<Boolean> full = OperatorFactory.wrap(Boolean.FALSE);

	@Override
	public String resolve() {
        return full.resolve() ? FakerUtil.getValue("address.country_code"): FakerUtil.getValue("address.country");

	}

}
