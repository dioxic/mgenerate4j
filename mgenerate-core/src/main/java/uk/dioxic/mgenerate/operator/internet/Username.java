package uk.dioxic.mgenerate.operator.internet;

import uk.dioxic.mgenerate.FakerUtil;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Username implements Resolvable<String> {

    @Override
    public String resolve() {
        return String.join(".",
                FakerUtil.getValue("name.first_name").replaceAll("'", "").toLowerCase(),
                FakerUtil.getValue("name.last_name").replaceAll("'", "").toLowerCase());
    }
}
