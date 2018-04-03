package uk.dioxic.mgenerate.operator.text;

import uk.dioxic.mgenerate.FakerUtil;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Word implements Resolvable<String> {

    @Override
    public String resolve() {
        return FakerUtil.getFake("lorem.words");
    }

}
