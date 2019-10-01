package uk.dioxic.mgenerate.core;

import org.assertj.core.api.Assertions;
import org.bson.json.JsonWriterSettings;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ParsingTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static JsonWriterSettings jws = JsonWriterSettings.builder()
            .indent(true)
            .build();

    private Path getResourcePath(String filename) throws URISyntaxException {
        URI uri = Objects.requireNonNull(getClass().getClassLoader().getResource(filename)).toURI();
        return Paths.get(uri);
    }

    @Test
    void dotNotationTest() throws URISyntaxException, IOException {
        Template template = Template.from(getResourcePath("dot-notation-test.json"));
        logger.debug(template.getDocument().toString());

        String outJson = template.toJson(jws);
        logger.info(outJson);
    }

    @Test
    void dotNotation3Test() throws URISyntaxException, IOException {
        Template template = Template.from(getResourcePath("dot-notation-test3.json"));
        logger.debug(template.getDocument().toString());

        String outJson = template.toJson(jws);
        logger.info(outJson);
    }

    @Test
    void documentTest() throws URISyntaxException, IOException {
        Template template = Template.from(getResourcePath("template.json"));
        logger.debug(template.getDocument().toString());

        String outJson = template.toJson(jws);
        logger.info(outJson);
    }

    @Test
    void optionalityTest() throws URISyntaxException, IOException {
        Template template = Template.from(getResourcePath("optionality-test.json"));
        logger.debug(template.getDocument().toString());

        String outJson = template.toJson(jws);
        Assertions.assertThat(outJson).doesNotContain("SHOULD_NOT_EXIST");

        logger.info(outJson);
    }

    @Test
    //@ExtendWith(TimingExtension.class)
    void performanceTest() throws URISyntaxException, IOException {
        Template template = Template.from(getResourcePath("pickset-test.json"));
        List<String> results = Stream.generate(template::toJson)
                .limit(100)
                .parallel()
                .collect(Collectors.toList());

        results.stream().findFirst().ifPresent(logger::debug);
    }

}
