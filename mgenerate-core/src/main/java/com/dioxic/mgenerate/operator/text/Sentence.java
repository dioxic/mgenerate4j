package com.dioxic.mgenerate.operator.text;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Operator
public class Sentence implements Resolvable<String> {

    @OperatorProperty
    Resolvable<Integer> words = OperatorFactory.wrap(12);

    @Override
    public String resolve() {
        return Stream.generate(() -> FakerUtil.getFake("lorem.word"))
                .limit(words.resolve())
                .collect(Collectors.joining(" ","","."));
    }

}
