package uk.dioxic.mgenerate.operator.text;

import uk.dioxic.mgenerate.util.FakerUtil;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Word implements Resolvable<String> {

    @Override
    public String resolve() {
        return FakerUtil.getValue("lorem.words");
    }

}
