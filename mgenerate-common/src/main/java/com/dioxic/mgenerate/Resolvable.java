package com.dioxic.mgenerate;

import com.dioxic.mgenerate.transformer.OperatorTransformer;
import org.bson.Document;
import org.bson.codec.OperatorCodecProvider;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.json.StrictJsonReader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.Arrays.asList;

public interface Resolvable<T> {

    T resolve();

    static Document parse(String file) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
//        String json = "{ \"name\": \"$email\"}";

        CodecRegistry registry = CodecRegistries.fromProviders(asList(new ValueCodecProvider(),
                //new BsonValueCodecProvider(),
                new DocumentCodecProvider(new OperatorTransformer()),
                new OperatorCodecProvider()));

        DocumentCodec codec = new DocumentCodec(registry, new BsonTypeClassMap(), new OperatorTransformer());

        //        Document doc = Document.parse(json, codec);
        StrictJsonReader bsonReader = new StrictJsonReader(json);
        return codec.decode(bsonReader, DecoderContext.builder().build());
    }

}
