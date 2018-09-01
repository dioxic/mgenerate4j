package uk.dioxic.mgenerate.core;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.faker.resolvable.Resolvable;
import uk.dioxic.mgenerate.core.DocumentValueCache;
import uk.dioxic.mgenerate.core.StringResolver;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import static org.assertj.core.api.Assertions.*;

public class StringResolverTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void resolve() {
        Resolvable lookup = new StringResolver("${nested.a} fish #{name.name}", FakerUtil.instance());
        Document nested = new Document("a", 123);
        Document document = new Document();
        document.put("lkp", lookup);
        document.put("nested", nested);

        DocumentValueCache.mapDocument(document);
        DocumentValueCache.setEncodingContext(document);

        assertThat(lookup.resolve()).as("string").isInstanceOf(String.class);
        assertThat(lookup.resolve().toString()).as("equal").startsWith("123 fish");

        logger.debug(lookup.resolve().toString());
    }
}
