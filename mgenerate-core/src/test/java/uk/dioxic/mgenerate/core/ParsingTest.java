package uk.dioxic.mgenerate.core;

import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;
import org.bson.json.JsonWriter;
import org.bson.json.JsonWriterSettings;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.core.codec.OperatorCodec;
import uk.dioxic.mgenerate.core.operator.internet.Email;

import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ParsingTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static JsonWriterSettings jws = JsonWriterSettings.builder()
            .indent(true)
            .build();

    @Test
    void encoderTest() {
        Codec<Resolvable> codec = new OperatorCodec();
        Email email = new Email();

        String output = encode(email, codec);

        logger.debug(output);
    }

    @Test
    void documentTest() throws URISyntaxException {
        Template template = Template.from(Paths.get(getClass().getClassLoader().getResource("template.json").toURI()));
        logger.debug(template.getDocument().toString());

        String outJson = template.toJson(jws);
        logger.info(outJson);
    }

    @Test
    //@ExtendWith(TimingExtension.class)
    void performanceTest() throws URISyntaxException {
        Template template = Template.from(Paths.get(getClass().getClassLoader().getResource("bson-test.json").toURI()));
        List<String> results = Stream.generate(template::toJson)
                .limit(100)
                .parallel()
                .collect(Collectors.toList());

        results.stream().findFirst().ifPresent(logger::debug);
    }

    <T> String encode(T source, Codec<T> codec) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter, jws);

        writer.writeStartDocument();
        writer.writeName("name");
        codec.encode(writer, source, EncoderContext.builder().build());
        writer.writeEndDocument();
        writer.close();

        return stringWriter.toString();
    }

}
