package com.dioxic.mgenerate.operator.internet;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Email implements Resolvable<String> {

    @OperatorProperty
    Resolvable<String> domain = FakerUtil.getFakeResolvable("internet.free_email");

    private Username username = new Username();

    @Override
	public String resolve() {
		return String.join("@", username.resolve(), domain.resolve());
	}

}
