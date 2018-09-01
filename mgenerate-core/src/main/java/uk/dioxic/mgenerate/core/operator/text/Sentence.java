package uk.dioxic.mgenerate.core.operator.text;

import uk.dioxic.mgenerate.core.util.FakerUtil;
import uk.dioxic.mgenerate.common.OperatorFactory;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.faker.resolvable.Resolvable;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Operator
public class Sentence implements Resolvable<String> {

    @OperatorProperty
    Resolvable<Integer> words = OperatorFactory.wrap(12);

    @Override
    public String resolve() {
        return Stream.generate(() -> FakerUtil.getValue("lorem.words"))
                .limit(words.resolve())
                .collect(Collectors.joining(" ","","."));
    }

}
