package uk.dioxic.mgenerate.core.operator.internet;

import uk.dioxic.mgenerate.common.Cache;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator
public class Domain implements Resolvable<String> {

    @Override
    public String resolve() {
        return String.join(".",
                "www",
                FakerUtil.getValue("name.last_name").replace("'", "").toLowerCase(),
                FakerUtil.getValue("internet.domain_suffix"));
    }

    @Override
    public String resolve(Cache cache) {
        return resolve();
    }

}
