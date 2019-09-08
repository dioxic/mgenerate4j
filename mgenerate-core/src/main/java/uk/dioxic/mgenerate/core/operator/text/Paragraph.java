package uk.dioxic.mgenerate.core.operator.text;

import uk.dioxic.mgenerate.common.State;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Operator
public class Paragraph implements Resolvable<String> {

    @OperatorProperty
    Resolvable<Integer> sentences = Wrapper.wrap(3);

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
