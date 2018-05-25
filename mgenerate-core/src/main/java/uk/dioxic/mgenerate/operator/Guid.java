package uk.dioxic.mgenerate.operator;

import uk.dioxic.faker.resolvable.Resolvable;
import uk.dioxic.mgenerate.annotation.Operator;

import java.util.UUID;

@Operator
public class Guid implements Resolvable<String> {

    @Override
    public String resolve() {
        return UUID.randomUUID().toString();
    }
}
