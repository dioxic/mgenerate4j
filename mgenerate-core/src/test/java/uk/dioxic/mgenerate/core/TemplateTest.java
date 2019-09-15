package uk.dioxic.mgenerate.core;

import org.bson.RawBsonDocument;
import org.bson.json.JsonWriterSettings;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.nio.file.Paths;

class TemplateTest {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static JsonWriterSettings jws = JsonWriterSettings.builder()
            .indent(true)
            .build();

    @Test
    void templateTest() throws URISyntaxException {

        Template template = Template.from(Paths.get(getClass().getClassLoader().getResource("lookup-test.json").toURI()));

        RawBsonDocument rbd = template.generateOne();

        System.out.println(rbd);

    }
}
