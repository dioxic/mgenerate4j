package com.dioxic.mgenerate.test;

import com.dioxic.mgenerate.operator.Email;
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
import com.dioxic.mgenerate.Transformer.OperatorTransformer;
import org.bson.json.StrictJsonReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class BsonTest {

    private static JsonWriterSettings jws = JsonWriterSettings.builder()
            .indent(true)
            .build();

    @Test
    public void encoderTest() throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("src/test/resources/bson-test.json")), StandardCharsets.UTF_8);

        Document doc = Document.parse(json);

        Codec codec = new OperatorCodec();
        Email email = new Email();

        String output = encode(email, codec);

        System.out.println(output);
    }

    @Test
    public void documentTest() throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("src/test/resources/bson-test.json")), StandardCharsets.UTF_8);
//        String json = "{ \"name\": \"$email\"}";

        CodecRegistry registry = CodecRegistries.fromProviders(asList(new ValueCodecProvider(),
                //new BsonValueCodecProvider(),
                new DocumentCodecProvider(new OperatorTransformer()),
                new OperatorCodecProvider()));

        DocumentCodec codec = new DocumentCodec(registry, new BsonTypeClassMap(), new OperatorTransformer());

//        Document doc = Document.parse(json, codec);
        StrictJsonReader bsonReader = new StrictJsonReader(json);
        Document doc = codec.decode(bsonReader, DecoderContext.builder().build());

        System.out.println(doc.toString());

        String outJson = doc.toJson(jws, codec);
        System.out.println(outJson);
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

    @SuppressWarnings("resource")
    protected <T> void writeReadCompare(T source, Codec<T> codec) {
        BasicOutputBuffer bsonOutput = new BasicOutputBuffer();
        BsonBinaryWriter writer = new BsonBinaryWriter(bsonOutput);
        writer.writeStartDocument();
        writer.writeName("name");
        codec.encode(writer, source, EncoderContext.builder().build());
        writer.writeEndDocument();
        writer.close();

        BsonBinaryReader reader = new BsonBinaryReader(ByteBuffer.wrap(bsonOutput.toByteArray()));
        reader.readStartDocument();
        assertThat(reader.readName()).isEqualTo("name");
        T readNow = codec.decode(reader, DecoderContext.builder().build());

        assertThat(readNow).isEqualTo(source);
    }
}
