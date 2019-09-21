package uk.dioxic.mgenerate.core.operator.text;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Operator
public class Sentence implements Resolvable<java.lang.String> {

    @OperatorProperty
    Resolvable<Integer> words = Wrapper.wrap(12);

    @Override
    public java.lang.String resolve() {
        return Stream.generate(() -> FakerUtil.getValue("lorem.words"))
                .limit(words.resolve())
                .collect(Collectors.joining(" ","","."));
    }

}
