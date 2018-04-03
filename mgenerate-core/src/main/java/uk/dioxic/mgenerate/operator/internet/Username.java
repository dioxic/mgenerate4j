package uk.dioxic.mgenerate.operator.internet;

import uk.dioxic.mgenerate.FakerUtil;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Username implements Resolvable<String> {

    @Override
    public String resolve() {
        return String.join(".",
                FakerUtil.getFake("name.first_name").replaceAll("'", "").toLowerCase(),
                FakerUtil.getFake("name.last_name").replaceAll("'", "").toLowerCase());
    }
}
