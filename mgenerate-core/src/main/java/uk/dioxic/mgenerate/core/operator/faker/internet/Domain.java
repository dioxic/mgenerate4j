package uk.dioxic.mgenerate.core.operator.faker.internet;

import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator
public class Domain extends AbstractOperator<String> {

    @Override
    public String resolveInternal() {
        return String.join(".",
                "www",
                FakerUtil.getValue("name.last_name").replace("'", "").toLowerCase(),
                FakerUtil.getValue("internet.domain_suffix"));
    }

}
