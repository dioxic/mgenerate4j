package uk.dioxic.mgenerate.operator.text;

import uk.dioxic.mgenerate.util.FakerUtil;
import uk.dioxic.mgenerate.OperatorFactory;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Operator
public class Sentence implements Resolvable<String> {

    @OperatorProperty
    Resolvable<Integer> words = OperatorFactory.wrap(12);

    @Override
    public String resolve() {
        return Stream.generate(() -> FakerUtil.getValue("lorem.word"))
                .limit(words.resolve())
                .collect(Collectors.joining(" ","","."));
    }

}
