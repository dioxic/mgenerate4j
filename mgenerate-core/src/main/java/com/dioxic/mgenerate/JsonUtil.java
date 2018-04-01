package com.dioxic.mgenerate;

import com.dioxic.mgenerate.codec.OperatorCodecProvider;
import com.dioxic.mgenerate.codec.OperatorTransformer;
import org.bson.Document;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.json.JsonWriterSettings;
import org.bson.json.StrictJsonReader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.Arrays.asList;

public class JsonUtil {

    private static final DocumentCodec codec = getDocumentCodec();

    public static CodecRegistry getRegistry() {
        return CodecRegistries.fromProviders(asList(new ValueCodecProvider(),
                new DocumentCodecProvider(new OperatorTransformer()),
                new OperatorCodecProvider()));
    }

    public static DocumentCodec getDocumentCodec() {
        return new DocumentCodec(getRegistry(), new BsonTypeClassMap(), new OperatorTransformer());
    }

    public static Document parse(String json) {
        StrictJsonReader bsonReader = new StrictJsonReader(json);
        return codec.decode(bsonReader, DecoderContext.builder().build());
    }

    public static Document parseFile(String file) throws IOException {
        return parse(new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8));
    }

    public static String toJson(Document document) {
        return document.toJson(codec);
    }

    public static String toJson(Document document, JsonWriterSettings jsonWriterSettings) {
        return document.toJson(jsonWriterSettings, codec);
    }
}
