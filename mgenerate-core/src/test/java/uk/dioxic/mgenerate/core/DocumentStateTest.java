package uk.dioxic.mgenerate.core;

import org.bson.json.JsonWriterSettings;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class DocumentStateTest {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static JsonWriterSettings jws = JsonWriterSettings.builder()
            .indent(true)
            .build();

    @Test
    public void documentTest() throws URISyntaxException {
        Template template = Template.from(Paths.get(getClass().getClassLoader().getResource("lookup-test.json").toURI()));

        String outJson = template.toJson(jws);
        logger.debug(outJson);

        DocumentStateCache.setEncodingContext(template);

        Object cachedValue = DocumentStateCache.get("c3");
        Object expected  = String.format("%s <%s>", DocumentStateCache.get("b"), DocumentStateCache.get("c.cc.ccc"));
        assertThat(cachedValue).as("is resolvable").isEqualTo(expected);
    }
}
