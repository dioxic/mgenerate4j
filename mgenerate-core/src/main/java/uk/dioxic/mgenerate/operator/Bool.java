package uk.dioxic.mgenerate.operator;

import uk.dioxic.mgenerate.util.FakerUtil;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Bool implements Resolvable<Boolean> {

	@Override
	public Boolean resolve() {
		return FakerUtil.randomBoolean();
	}

}
