package uk.dioxic.mgenerate.core.operator.person;

import uk.dioxic.mgenerate.core.util.FakerUtil;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class MartialStatus implements Resolvable<String> {

    @Override
    public String resolve() {
        return FakerUtil.getValue("demographic.marital_status");
    }

}
