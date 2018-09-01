package uk.dioxic.mgenerate.core.operator.person;

import uk.dioxic.mgenerate.core.util.FakerUtil;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Gender implements Resolvable<String> {

    @Override
    public String resolve() {
        return FakerUtil.getValue("demographic.sex");
    }

}
