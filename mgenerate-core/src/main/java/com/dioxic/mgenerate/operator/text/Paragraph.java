package com.dioxic.mgenerate.operator.text;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import com.dioxic.mgenerate.Resolvable;

@OperatorClass
public class Paragraph implements Resolvable<String> {

    @OperatorProperty
    Resolvable<Integer> sentences = OperatorFactory.wrap(1);

    @Override
    public String resolve() {
        return FakerUtil.instance().lorem().paragraph(sentences.resolve());
    }

}
