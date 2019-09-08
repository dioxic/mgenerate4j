package uk.dioxic.mgenerate.core.resolver;

import uk.dioxic.faker.Faker;
import uk.dioxic.mgenerate.common.Cache;
import uk.dioxic.mgenerate.common.Resolvable;

public class LookupResolver implements Resolvable<String> {

    private final String lookupKey;
    private final Faker faker;

    public LookupResolver(String lookupKey, Faker faker) {
        this.lookupKey = lookupKey;
        this.faker = faker;
    }

    @Override
    public String resolve() {
        return faker.get(lookupKey);
    }

    @Override
    public String resolve(Cache cache) {
        return resolve();
    }
}