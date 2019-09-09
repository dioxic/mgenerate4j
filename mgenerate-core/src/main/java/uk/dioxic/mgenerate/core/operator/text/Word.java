package uk.dioxic.mgenerate.core.operator.text;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator
public class Word implements Resolvable<String> {

    @Override
    public String resolve() {
        return FakerUtil.getValue("lorem.words");
    }

}
