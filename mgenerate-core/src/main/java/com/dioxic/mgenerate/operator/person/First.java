package com.dioxic.mgenerate.operator.person;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;

import java.time.ZoneOffset;
import java.util.Date;

@Operator
public class First implements Resolvable<String> {

    @Override
    public String resolve() {
        return FakerUtil.instance().name().firstName();
    }

}
