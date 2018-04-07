package uk.dioxic.mgenerate;

import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.json.JsonWriter;
import org.bson.json.JsonWriterSettings;
import org.bson.json.StrictJsonReader;
import uk.dioxic.mgenerate.codec.DocumentCacheCodec;
import uk.dioxic.mgenerate.codec.DocumentCacheCodecProvider;
import uk.dioxic.mgenerate.codec.OperatorCodecProvider;
import uk.dioxic.mgenerate.codec.OperatorTransformer;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.Arrays.asList;

public class BsonUtil {

    private static final DocumentCacheCodec DEFAULT_CODEC = getDocumentCodec();
    private static final JsonWriterSettings DEFAULT_JWS = JsonWriterSettings.builder().build();

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
        return DEFAULT_CODEC.decodeAndMap(bsonReader, DecoderContext.builder().build());
    }

    public static Document parseFile(String file) throws IOException {
        return parse(new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8));
    }

    public static String toJson(Document document) {
        return toJson(document, DEFAULT_JWS);
    }

    public static String toJson(Document document, JsonWriterSettings jsonWriterSettings) {
        return toJson(document, jsonWriterSettings, true);
    }

    public static String toJson(Document document, boolean encodingCollectibleDocument) {
        return toJson(document, DEFAULT_JWS, encodingCollectibleDocument);
    }

    public static String toJson(Document document, JsonWriterSettings jsonWriterSettings, boolean encodingCollectibleDocument) {
        JsonWriter writer = new JsonWriter(new StringWriter(), jsonWriterSettings);
        DEFAULT_CODEC.encode(writer, document, EncoderContext.builder().isEncodingCollectibleDocument(encodingCollectibleDocument).build());
        return writer.getWriter().toString();
    }

}
