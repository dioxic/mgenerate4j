package uk.dioxic.mgenerate.operator.internet;

import uk.dioxic.mgenerate.util.FakerUtil;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Domain implements Resolvable<String> {

    @Override
    public String resolve() {
        return String.join(".",
                "www",
                FakerUtil.getValue("name.last_name").replace("'", "").toLowerCase(),
                FakerUtil.getValue("internet.domain_suffix"));
    }

}
