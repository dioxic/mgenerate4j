package uk.dioxic.mgenerate.core;

import uk.dioxic.faker.Faker;
import uk.dioxic.faker.resolvable.LookupResolver;
import uk.dioxic.faker.resolvable.Resolvable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringResolver implements Resolvable {
    private final static Pattern LOOKUP_PATTERN = Pattern.compile("([#$])\\{([a-z0-9A-Z_.]+)\\s*}");

    private final List<String> parts;
    private final List<Resolvable> lookups;

    public StringResolver(String expression, Faker faker) {
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

        Iterator<Resolvable> iter = lookups.iterator();
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

    private static class DocumentKeyResolver implements Resolvable {
        private final String documentKey;

        public DocumentKeyResolver(String documentKey) {
            this.documentKey = documentKey;
        }

        @Override
        public Object resolve() {
            return DocumentValueCache.get(documentKey);
        }
    }
}
