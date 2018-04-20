package uk.dioxic.mgenerate.operator.text;

import uk.dioxic.mgenerate.util.FakerUtil;
import uk.dioxic.mgenerate.OperatorFactory;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;
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
                Stream.generate(() -> FakerUtil.getValue("lorem.words"))
                        .limit(FakerUtil.numberBetween(12, 18))
                        .collect(Collectors.joining(" ", "", ".")))
                .limit(sentences.resolve())
                .collect(Collectors.joining(" "));
    }

}
