package uk.dioxic.mgenerate.core.resolver;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.core.VariableCache;

public class VariableResolver implements Resolvable<Object> {

    private final String name;

    public VariableResolver(String name) {
        this.name = (name.startsWith("$$")) ? name.substring(2) : name;
    }

    @Override
    public Object resolve() {
        return VariableCache.get(name);
    }

}