package com.dioxic.mgenerate.operator.text;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import com.dioxic.mgenerate.operator.Operator;

import java.util.List;

@OperatorClass
public class Word implements Operator<List<String>> {

    @OperatorProperty
    Operator<Integer> number = OperatorFactory.wrap(1);

    @Override
    public List<String> resolve() {
        return FakerUtil.instance().lorem().words(number.resolve());
    }

}
