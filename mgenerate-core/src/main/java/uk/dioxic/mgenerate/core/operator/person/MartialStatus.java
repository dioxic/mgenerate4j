package uk.dioxic.mgenerate.core.operator.person;

import uk.dioxic.mgenerate.common.Cache;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator
public class MartialStatus implements Resolvable<String> {

    @Override
    public String resolve() {
        return FakerUtil.getValue("demographic.marital_status");
    }

    @Override
    public String resolve(Cache cache) {
        return resolve();
    }

}
