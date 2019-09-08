package uk.dioxic.mgenerate.core.operator.person;

import uk.dioxic.mgenerate.common.State;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator
public class Prefix implements Resolvable<String> {

    @Override
    public String resolve() {
        return FakerUtil.getValue("name.prefix");
    }

}
