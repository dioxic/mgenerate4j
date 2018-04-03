package uk.dioxic.mgenerate.operator.person;

import uk.dioxic.mgenerate.FakerUtil;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class First implements Resolvable<String> {

    @Override
    public String resolve() {
        return FakerUtil.getFake("name.first_name");
    }

}
