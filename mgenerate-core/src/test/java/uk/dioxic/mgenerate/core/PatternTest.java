package uk.dioxic.mgenerate.core;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.core.resolver.PatternResolver;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import static org.assertj.core.api.Assertions.*;

public class PatternTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void resolve() {
        Resolvable lookup = new PatternResolver("${nested.a} fish #{name.name}", FakerUtil.instance());
        Document nested = new Document("a", 123);
        Document document = new Document();
        document.put("lkp", lookup);
        document.put("nested", nested);

        DocumentValueCache.getInstance().mapTemplate(document);
        DocumentValueCache.getInstance().setEncodingContext(document);

        assertThat(lookup.resolve()).as("string").isInstanceOf(String.class);
        assertThat(lookup.resolve().toString()).as("equal").startsWith("123 fish");

        logger.debug(lookup.resolve().toString());
    }
}
