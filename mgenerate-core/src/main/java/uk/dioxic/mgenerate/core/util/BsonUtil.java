package uk.dioxic.mgenerate.core.util;

import org.bson.Document;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.json.JsonReader;
import org.bson.json.JsonWriter;
import org.bson.json.JsonWriterSettings;
import uk.dioxic.mgenerate.core.codec.*;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class BsonUtil {

    private static final DocumentCacheCodec DEFAULT_CODEC = getDocumentCodec();
    private static final JsonWriterSettings DEFAULT_JWS = JsonWriterSettings.builder().indent(true).build();

    public static CodecRegistry getRegistry() {
        return CodecRegistries.fromProviders(asList(new ValueCodecProvider(),
                new BsonValueCodecProvider(),
                new DocumentCacheCodecProvider(new OperatorTransformer()),
                new ExtendedCodecProvider(),
                new OperatorCodecProvider()));
    }

    public static DocumentCacheCodec getDocumentCodec() {
        return new DocumentCacheCodec(getRegistry(), new BsonTypeClassMap(), new OperatorTransformer());
    }

    public static Document parse(String json) {
        return parse(json, DEFAULT_CODEC);
    }

    public static <T> T parse(String json, Codec<T> codec) {
        JsonReader bsonReader = new JsonReader(json);
        return codec.decode(bsonReader, DecoderContext.builder().build());
    }

    public static <T> T parseFile(String file, Codec<T> codec) {
        return parseFile(Paths.get(file), codec);
    }

    public static Document parseFile(String file) {
        return parseFile(Paths.get(file), DEFAULT_CODEC);
    }

    public static Document parseFile(Path file) {
        return parseFile(file, DEFAULT_CODEC);
    }

    public static <T> T parseFile(Path file, Codec<T> codec) {
        try {
            return parse(new String(Files.readAllBytes(file), StandardCharsets.UTF_8), codec);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    /**
     * Maps the input {@link Document} to a one-dimension map suitable for dot-notation lookups.
     * @param document the document to map
     */
    public static Map<String, Object> flatMap(Document document) {
        Map<String, Object> flatMap = new HashMap<>();

        map(flatMap, null, document);

        return flatMap;
    }

    @SuppressWarnings("unchecked")
    private static void map(Map<String, Object> flatMap, String key, Object o) {
        if (o instanceof Map) {
            Map<String, Object> localMap = ((Map) o);

            localMap.entrySet().forEach(m -> map(flatMap, key, m));
            flatMap.put(key, o);
        }
        else if (o instanceof Map.Entry) {
            Map.Entry entry = ((Map.Entry) o);
            map(flatMap, getNewKey(key, entry), entry.getValue());
        }
        else if (o instanceof List) {
            int counter = 0;
            for (Object item : (List)o) {
                map(flatMap, key + "." + counter++, item);
            }
        }
        else {
            if (key != null) {
                flatMap.put(key, o);
            }
        }
    }

    private static String getNewKey(String key, Map.Entry<String, Object> entry) {
        String newKey = key == null ? entry.getKey() : key + "." + entry.getKey();
        return newKey.replace('-', '_');
    }

}
