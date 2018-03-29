package com.dioxic.mgenerate.operator.text;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import com.dioxic.mgenerate.operator.Operator;

@OperatorClass
public class Paragraph implements Operator<String> {

    @OperatorProperty
    Operator<Integer> sentences = OperatorFactory.wrap(1);

    @Override
    public String resolve() {
        return FakerUtil.instance().lorem().paragraph(sentences.resolve());
    }

}
