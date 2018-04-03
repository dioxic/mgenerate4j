package com.dioxic.mgenerate.operator.person;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Last implements Resolvable<String> {

    @Override
    public String resolve() {
        return FakerUtil.getFake("name.last_name");
    }

}
