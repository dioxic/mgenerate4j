package uk.dioxic.mgenerate.core.operator;

import uk.dioxic.faker.resolvable.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;

import java.util.UUID;

@Operator
public class Guid implements Resolvable<String> {

    @Override
    public String resolve() {
        return UUID.randomUUID().toString();
    }
}
