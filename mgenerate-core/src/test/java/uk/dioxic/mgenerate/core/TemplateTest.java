package uk.dioxic.mgenerate.core;

import org.bson.RawBsonDocument;
import org.bson.json.JsonWriterSettings;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

class TemplateTest {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static JsonWriterSettings jws = JsonWriterSettings.builder()
            .indent(true)
            .build();

    @Test
    void templateTest() throws URISyntaxException, IOException {

        Template template = Template.from(Paths.get(getClass().getClassLoader().getResource("template.json").toURI()));

        String outJson = template.toJson(jws);
        logger.info(outJson);

    }
}
