package uk.dioxic.mgenerate.core.operator.faker.internet;

import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator
public class Username extends AbstractOperator<String> {

    @Override
    public String resolveInternal() {
        return String.join(".",
                FakerUtil.getValue("name.first_name").replaceAll("'", "").toLowerCase(),
                FakerUtil.getValue("name.last_name").replaceAll("'", "").toLowerCase());
    }

}
