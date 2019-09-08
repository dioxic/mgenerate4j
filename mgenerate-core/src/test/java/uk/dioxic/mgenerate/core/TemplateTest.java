package uk.dioxic.mgenerate.core;

import org.bson.Document;
import org.bson.RawBsonDocument;
import org.bson.json.JsonWriterSettings;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.mgenerate.core.util.BsonUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class TemplateTest {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static JsonWriterSettings jws = JsonWriterSettings.builder()
            .indent(true)
            .build();

    @Test
    public void templateTest() throws URISyntaxException {

        Template template = Template.from(Paths.get(getClass().getClassLoader().getResource("lookup-test.json").toURI()));

        RawBsonDocument rbd = template.generateOne();

        System.out.println(rbd);

    }
}
