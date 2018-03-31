package com.dioxic.mgenerate.operator.person;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.Operator;

@Operator
public class MartialStatus implements Resolvable<String> {

    @Override
    public String resolve() {
        return FakerUtil.instance().demographic().maritalStatus();
    }

}
