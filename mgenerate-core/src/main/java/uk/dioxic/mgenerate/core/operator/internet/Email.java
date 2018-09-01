package uk.dioxic.mgenerate.core.operator.internet;

import uk.dioxic.mgenerate.core.util.FakerUtil;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Email implements Resolvable<String> {

    @OperatorProperty
    Resolvable<String> domain = FakerUtil.getResolvable("internet.free_email");

    private Username username = new Username();

    @Override
	public String resolve() {
		return String.join("@", username.resolve(), domain.resolve());
	}

}
