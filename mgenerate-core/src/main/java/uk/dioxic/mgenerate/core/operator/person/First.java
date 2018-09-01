package uk.dioxic.mgenerate.core.operator.person;

import uk.dioxic.mgenerate.core.util.FakerUtil;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class First implements Resolvable<String> {

    @Override
    public String resolve() {
        return FakerUtil.getValue("name.first_name");
    }

}
