package uk.dioxic.mgenerate;

import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.json.JsonWriterSettings;
import org.bson.json.StrictJsonReader;
import uk.dioxic.mgenerate.codec.DocumentCacheCodec;
import uk.dioxic.mgenerate.codec.DocumentCacheCodecProvider;
import uk.dioxic.mgenerate.codec.OperatorCodecProvider;
import uk.dioxic.mgenerate.codec.OperatorTransformer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.Arrays.asList;

public class BsonUtil {

    private static final DocumentCacheCodec codec = getDocumentCodec();

    public static CodecRegistry getRegistry() {
        return CodecRegistries.fromProviders(asList(new ValueCodecProvider(),
                new DocumentCacheCodecProvider(new OperatorTransformer()),
                new OperatorCodecProvider()));
    }

    public static DocumentCacheCodec getDocumentCodec() {
        return new DocumentCacheCodec(getRegistry(), new BsonTypeClassMap(), new OperatorTransformer());
    }

    public static Document parse(String json) {
        StrictJsonReader bsonReader = new StrictJsonReader(json);
        return codec.decodeAndMap(bsonReader, DecoderContext.builder().build());
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
