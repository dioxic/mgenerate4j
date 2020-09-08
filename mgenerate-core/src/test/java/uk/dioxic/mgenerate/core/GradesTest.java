package uk.dioxic.mgenerate.core;

import org.bson.json.JsonWriterSettings;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class GradesTest {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static JsonWriterSettings jws = JsonWriterSettings.builder()
            .indent(true)
            .build();

    @Test
    void gradesTest() throws URISyntaxException, IOException {

        Template template = Template.from(Paths.get(getClass().getClassLoader().getResource("templates/grades.json").toURI()));

        String outJson = template.toJson(jws);



        logger.info(outJson);

    }
}
