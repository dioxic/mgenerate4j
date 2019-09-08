package uk.dioxic.mgenerate.core.operator.internet;

import uk.dioxic.mgenerate.common.Cache;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator
public class Email implements Resolvable<String> {

    @OperatorProperty
    Resolvable<String> domain = FakerUtil.getResolvable("internet.free_email");

    private Username username = new Username();

    @Override
	public String resolve(Cache cache) {
		return String.join("@", username.resolve(cache), domain.resolve(cache));
	}

    @Override
    public String resolve() {
        return resolve(null);
    }

}
