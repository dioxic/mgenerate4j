package com.dioxic.mgenerate.operator.text;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Operator
public class Paragraph implements Resolvable<String> {

    @OperatorProperty
    Resolvable<Integer> sentences = OperatorFactory.wrap(3);

    @Override
    public String resolve() {
        return Stream.generate(() ->
                Stream.generate(() -> FakerUtil.getFake("lorem.word"))
                        .limit(FakerUtil.numberBetween(12, 18))
                        .collect(Collectors.joining(" ", "", ".")))
                .limit(sentences.resolve())
                .collect(Collectors.joining(" "));
    }

}
