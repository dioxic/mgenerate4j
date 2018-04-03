package com.dioxic.mgenerate;

import uk.dioxic.mgenerate.JsonUtil;
import uk.dioxic.mgenerate.codec.OperatorCodec;
import uk.dioxic.mgenerate.operator.internet.Email;
import com.dioxic.mgenerate.test.TimingExtension;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;
import org.bson.json.JsonWriter;
import org.bson.json.JsonWriterSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParsingTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static JsonWriterSettings jws = JsonWriterSettings.builder()
            .indent(true)
            .build();

    @Test
    public void encoderTest() {
        Codec codec = new OperatorCodec();
        Email email = new Email();

        String output = encode(email, codec);

        logger.debug(output);
    }

    @Test
    public void documentTest() throws IOException {
        Document doc = JsonUtil.parseFile("src/test/resources/template.json");
        logger.debug(doc.toString());

        String outJson = JsonUtil.toJson(doc, jws);
        logger.debug(outJson);
    }

    @Test
    @ExtendWith(TimingExtension.class)
    public void performanceTest() throws IOException {
        Document doc = JsonUtil.parseFile("src/test/resources/bson-test.json");

        List<String> results = Stream.generate(() -> JsonUtil.toJson(doc))
                .limit(100)
                .parallel()
                .collect(Collectors.toList());

        results.stream().findFirst().ifPresent(logger::debug);
    }

    protected <T> String encode(T source, Codec<T> codec) {
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
