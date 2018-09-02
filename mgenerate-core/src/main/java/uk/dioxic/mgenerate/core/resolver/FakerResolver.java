package uk.dioxic.mgenerate.core.resolver;

import uk.dioxic.faker.Faker;
import uk.dioxic.mgenerate.common.Resolvable;

public class FakerResolver implements Resolvable {

    private Faker faker;
    private String key;

    public FakerResolver(Faker faker, String key) {
        this.faker = faker;
        this.key = key;
    }

    @Override
    public Object resolve() {
        return faker.get(key);
    }
}
