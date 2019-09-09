package uk.dioxic.mgenerate.core;

import org.bson.BsonBinaryWriter;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.RawBsonDocument;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.Encoder;
import org.bson.codecs.EncoderContext;
import org.bson.io.BasicOutputBuffer;
import org.bson.json.JsonReader;
import org.bson.json.JsonWriter;
import org.bson.json.JsonWriterSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.core.codec.TemplateCodec;
import uk.dioxic.mgenerate.core.util.BsonUtil;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Template {

    private static final JsonWriterSettings DEFAULT_JWS = JsonWriterSettings.builder().indent(true).build();
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final TemplateCodec templateCodec;
    private final Document document;
    private final Map<String, Object> dotMap;
    private final Map<Resolvable, String> resolverCoordinateMap;
    private boolean stateCachingRequired = false;

    public static Template from(Path templateFile) {
        try {
            String json = new String(Files.readAllBytes(templateFile), StandardCharsets.UTF_8);
            return parse(json);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static Template parse(final String json) {
        return parse(json, new TemplateCodec());
    }

    private static Template parse(final String json, final Decoder<Template> decoder) {
        JsonReader bsonReader = new JsonReader(json);
        return decoder.decode(bsonReader, DecoderContext.builder().build());
    }

    public Template(Document template) {
        templateCodec = new TemplateCodec();
        this.document = template;
        this.dotMap = BsonUtil.flatMap(template);
        resolverCoordinateMap = new HashMap<>();
        dotMap.forEach((k, v) -> {
            if (v instanceof Resolvable) {
                resolverCoordinateMap.put((Resolvable) v, k);
            }
        });
        logger.debug(this.toString());
    }

    public Object getValue(String coordinates) {
        return dotMap.get(coordinates);
    }

    public String getCoordinates(Resolvable resolvable) {
        return resolverCoordinateMap.get(resolvable);
    }

    public Document getDocument() {
        return document;
    }

    /**
     * If true there are lookup resolvables that require calculated values to be cached
     * @return
     */
    public boolean isStateCachingRequired() {
        return stateCachingRequired;
    }

    public void setStateCachingRequired(boolean cachingRequired) {
        this.stateCachingRequired = cachingRequired;
    }

    public RawBsonDocument generateOne() {
        BasicOutputBuffer buffer = new BasicOutputBuffer();
        BsonWriter writer = new BsonBinaryWriter(buffer);
        templateCodec.encode(writer, this, EncoderContext.builder().build());
        return new RawBsonDocument(buffer.getInternalBuffer());
    }

    /**
     * Gets a hydrated JSON representation of this template
     *
     * @return a JSON representation of this document
     * @throws org.bson.codecs.configuration.CodecConfigurationException if the document contains types not in the default registry
     */
    public String toJson() {
        return toJson(DEFAULT_JWS, templateCodec);
    }

    /**
     * Gets a hydrated JSON representation of this template
     *
     * @param writerSettings the json writer settings to use when encoding
     * @return a JSON representation of this document
     * @throws org.bson.codecs.configuration.CodecConfigurationException if the document contains types not in the default registry
     */
    public String toJson(final JsonWriterSettings writerSettings) {
        return toJson(writerSettings, templateCodec);
    }

    /**
     * Gets a hydrated JSON representation of this template
     *
     * @param writerSettings the json writer settings to use when encoding
     * @param encoder the document codec instance to use to encode the document
     * @return a JSON representation of this document
     * @throws org.bson.codecs.configuration.CodecConfigurationException if the registry does not contain a codec for the document values.
     */
    public String toJson(final JsonWriterSettings writerSettings, final Encoder<Template> encoder) {
        JsonWriter writer = new JsonWriter(new StringWriter(), writerSettings);
        encoder.encode(writer, this, EncoderContext.builder().build());
        return writer.getWriter().toString();
    }

    @Override
    public String toString() {
        return "Template{" +
                "document=" + document +
                ", dotMap=" + dotMap +
                ", resolverCoordinateMap=" + resolverCoordinateMap +
                ", stateCachingRequired=" + stateCachingRequired +
                '}';
    }
}