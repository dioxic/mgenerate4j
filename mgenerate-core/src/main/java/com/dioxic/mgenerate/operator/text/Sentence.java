package com.dioxic.mgenerate.operator.text;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import com.dioxic.mgenerate.operator.Operator;

@OperatorClass
public class Sentence implements Operator<String> {

    @OperatorProperty
    Operator<Integer> words = OperatorFactory.wrap(1);

    @Override
    public String resolve() {
        return FakerUtil.instance().lorem().sentence(words.resolve());
    }

}
