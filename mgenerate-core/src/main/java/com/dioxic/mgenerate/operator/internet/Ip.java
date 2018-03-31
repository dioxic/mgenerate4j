package com.dioxic.mgenerate.operator.internet;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.Operator;

@Operator
public class Ip implements Resolvable<String> {

    @Override
    public String resolve() {
        return FakerUtil.instance().internet().ipV4Address();
    }

}
