package uk.dioxic.mgenerate.core.operator.location;

import uk.dioxic.mgenerate.common.Cache;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;
import uk.dioxic.mgenerate.core.util.FlsUtil;

@Operator
public class Country implements Resolvable<String> {

    @OperatorProperty
    Resolvable<Boolean> full = Wrapper.wrap(Boolean.FALSE);

	@Override
	public String resolve() {
        return full.resolve() ? FakerUtil.getValue("address.country_code"): FakerUtil.getValue("address.country");
	}

    @Override
    public String resolve(Cache cache) {
        return resolve();
    }

}
