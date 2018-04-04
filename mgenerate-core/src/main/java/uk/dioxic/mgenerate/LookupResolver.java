package uk.dioxic.mgenerate;

import uk.dioxic.faker.resolvable.Resolvable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LookupResolver implements Resolvable {

    private static final Pattern LOOKUP_PATTERN = Pattern.compile("\\{\\{([a-z0-9A-Z_.]+)\\s?(?:'([^']+)')*\\}\\}");
    private final String documentKey;

    public LookupResolver(String documentKey) {
        Matcher matcher = LOOKUP_PATTERN.matcher(documentKey);

        if (matcher.find()) {
            this.documentKey = matcher.group(1);
        } else {
            throw new IllegalStateException("cannot parse \"" + documentKey +"\"");
        }
    }

    @Override
    public Object resolve() {
        return DocumentValueCache.get(documentKey);
    }

    public static boolean canHandle(String expression) {
        return LOOKUP_PATTERN.matcher(expression).find();
    }

}
