package uk.dioxic.mgenerate.core.resolver;

import uk.dioxic.faker.Faker;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternResolver implements Resolvable {
    private final static Pattern LOOKUP_PATTERN = Pattern.compile("([#$])\\{([a-z0-9A-Z_.]+)\\s*}");

    private final List<String> parts;
    private final List<Resolvable<?>> lookups;

    public PatternResolver(String expression, Faker faker) {
        Matcher matcher = LOOKUP_PATTERN.matcher(expression);
        lookups = new ArrayList<>();

        while (matcher.find()) {
            switch (matcher.group(1)) {
                case "#":
                    lookups.add(new LookupResolver(matcher.group(2), faker));
                    break;
                case "$":
                    lookups.add(new DocumentKeyResolver(matcher.group(2)));
                    break;
            }
        }

        parts = Arrays.asList(LOOKUP_PATTERN.split(expression));

        if (lookups.isEmpty()) {
            throw new IllegalStateException("no lookups found in expression [" + expression + "]");
        }
    }

    @Override
    public Object resolve() {
        if (parts.isEmpty()) {
            return lookups.get(0).resolve();
        }

        StringBuilder sb = new StringBuilder();

        Iterator<Resolvable<?>> iter = lookups.iterator();
        for (String part : parts) {
            sb.append(part);
            if (iter.hasNext()) {
                sb.append(iter.next().resolve());
            }
        }

        return sb.toString();
    }

    public static boolean canHandle(String expression) {
        return LOOKUP_PATTERN.matcher(expression).find();
    }

    @Override
    public Object resolve(Cache cache) {
        if (parts.isEmpty()) {
            return lookups.get(0).resolve(cache);
        }

        StringBuilder sb = new StringBuilder();

        Iterator<Resolvable<?>> iter = lookups.iterator();
        for (String part : parts) {
            sb.append(part);
            if (iter.hasNext()) {
                sb.append(iter.next().resolve(cache));
            }
        }

        return sb.toString();
    }
}
