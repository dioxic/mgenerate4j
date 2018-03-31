package com.dioxic.mgenerate.operator.person;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.Operator;

@Operator
public class Name implements Resolvable<String> {

    @Override
    public String resolve() {
        return FakerUtil.instance().name().name();
    }

}
