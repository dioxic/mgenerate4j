package uk.dioxic.mgenerate.core.operator;

import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.util.FakerUtil;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Bool implements Resolvable<Boolean> {

	@Override
	public Boolean resolve() {
		return FakerUtil.randomBoolean();
	}

}
