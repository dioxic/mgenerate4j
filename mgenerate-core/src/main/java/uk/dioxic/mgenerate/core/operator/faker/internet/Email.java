package uk.dioxic.mgenerate.core.operator.faker.internet;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator
public class Email extends AbstractOperator<String> {

    @OperatorProperty
    Resolvable<String> domain = FakerUtil.getResolvable("internet.free_email");

    private Username username = new Username();

    @Override
	public String resolveInternal() {
		return String.join("@", username.resolveInternal(), domain.resolve());
	}

}
