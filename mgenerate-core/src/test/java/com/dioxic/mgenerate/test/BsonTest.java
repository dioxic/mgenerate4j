package com.dioxic.mgenerate.test;

import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.operator.internet.Email;
import org.bson.BsonBinaryReader;
import org.bson.BsonBinaryWriter;
import org.bson.Document;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.io.BasicOutputBuffer;
import org.bson.json.JsonWriter;
import org.bson.json.JsonWriterSettings;
import org.bson.codec.OperatorCodec;
import org.bson.codec.OperatorCodecProvider;
import com.dioxic.mgenerate.transformer.OperatorTransformer;
import org.bson.json.StrictJsonReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class BsonTest {

    private static CodecRegistry registry = CodecRegistries.fromProviders(asList(new ValueCodecProvider(),
            new DocumentCodecProvider(new OperatorTransformer()),
            new OperatorCodecProvider()));

    private static DocumentCodec codec = new DocumentCodec(registry, new BsonTypeClassMap(), new OperatorTransformer());

    private static JsonWriterSettings jws = JsonWriterSettings.builder()
            .indent(true)
            .build();

    private static Document parse(String file) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
            StrictJsonReader bsonReader = new StrictJsonReader(json);
            return codec.decode(bsonReader, DecoderContext.builder().build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void encoderTest() {
        Codec codec = new OperatorCodec();
        Email email = new Email();

        String output = encode(email, codec);

        System.out.println(output);
    }

    @Test
    public void documentTest() {
        Document doc = parse("src/test/resources/template.json");
        System.out.println(doc.toString());

        String outJson = doc.toJson(jws, codec);
        System.out.println(outJson);
    }

    @Test
    @ExtendWith(TimingExtension.class)
    public void performanceTest() {
        Document doc = parse("src/test/resources/bson-test.json");

        List<String> results = Stream.generate(() -> doc.toJson(jws, codec))
                .limit(1000)
                .parallel()
                .collect(Collectors.toList());

        //results.stream().limit(2).forEach(System.out::println);
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
