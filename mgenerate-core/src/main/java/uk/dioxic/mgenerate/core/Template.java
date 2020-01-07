package uk.dioxic.mgenerate.core;

import org.bson.BsonBinaryWriter;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.RawBsonDocument;
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
import uk.dioxic.mgenerate.core.util.DocumentUtil;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Template {

    private static final JsonWriterSettings DEFAULT_JWS = JsonWriterSettings.builder().build();
    private final TemplateCodec templateCodec;
    private final Document document;
    private final Map<String, Object> dotMap;
    private final Map<Resolvable, String> resolverCoordinateMap;
    private boolean stateCachingRequired;

    /**
     * Parses the input template file into a {@link Template}
     * @param templateFile {@link Path} to json file
     * @return {@link Template}
     * @throws IOException if the file cannot be processed
     */
    public static Template from(Path templateFile) throws IOException {
        String json = new String(Files.readAllBytes(templateFile), StandardCharsets.UTF_8);
        return parse(json);
    }

    /**
     * Parses the input template file into a {@link Template}.
     * @param templateFile a json-format file
     * @return {@link Template}
     * @throws IOException if the file cannot be processed
     */
    public static Template from(String templateFile) throws IOException {
        return from(Paths.get(templateFile));
    }

    /**
     * Creates a {@link Template} from a BSON document.
     * @param template a {@link Document} representation of a template
     * @return {@link Template}
     */
    public static Template from(Document template, boolean stateCachingRequired) {
        return new Template(template, stateCachingRequired);
    }

    /**
     * Parses the input json into a {@link Template}.
     * @param json json representation of a template
     * @return {@link Template}
     */
    public static Template parse(final String json) {
        JsonReader bsonReader = new JsonReader(json);
        return new TemplateCodec().decode(bsonReader, DecoderContext.builder().build());
    }

    private Template(Document template, boolean stateCachingRequired) {
        this.templateCodec = new TemplateCodec();
        this.document = template;
        this.dotMap = DocumentUtil.flatMap(template);
        this.resolverCoordinateMap = new HashMap<>();
        this.stateCachingRequired = stateCachingRequired;
        dotMap.forEach((k, v) -> {
            if (v instanceof Resolvable) {
                resolverCoordinateMap.put((Resolvable) v, k);
            }
        });
    }

    Object get(String coordinates) {
        return dotMap.get(coordinates);
    }

    boolean containsKey(String coordinates) {
        return dotMap.containsKey(coordinates);
    }

    String getCoordinates(Resolvable resolvable) {
        return resolverCoordinateMap.get(resolvable);
    }

    /**
     * @return the actual template document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * If true there are lookup resolvables that require calculated values to be cached.
     * @return is document state caching required
     */
    boolean isStateCachingRequired() {
        return stateCachingRequired;
    }

    /**
     * Gets a hydrated BSON representation of this template.
     *
     * @return a BSON representation of this document
     * @throws org.bson.codecs.configuration.CodecConfigurationException if the document contains types not in the default registry
     */
    public RawBsonDocument toRawBson() {
        BasicOutputBuffer buffer = new BasicOutputBuffer();
        encode(new BsonBinaryWriter(buffer));
        return new RawBsonDocument(buffer.getInternalBuffer());
    }

    private void encode(BsonWriter writer) {
        templateCodec.encode(writer, this, EncoderContext.builder().build());
    }

    /**
     * Gets a hydrated JSON representation of this template using the {@link org.bson.json.JsonMode#EXTENDED} output mode, and otherwise the default
     * settings of {@link JsonWriterSettings.Builder} and {@link TemplateCodec}.
     *
     * @return a JSON representation of this document
     * @throws org.bson.codecs.configuration.CodecConfigurationException if the document contains types not in the default registry
     */
    public String toJson() {
        return toJson(DEFAULT_JWS, templateCodec);
    }

    /**
     * Gets a hydrated JSON representation of this template.
     *
     * <p>With the default {@link TemplateCodec}.</p>
     *
     * @param writerSettings the json writer settings to use when encoding
     * @return a JSON representation of this document
     * @throws org.bson.codecs.configuration.CodecConfigurationException if the document contains types not in the default registry
     */
    public String toJson(final JsonWriterSettings writerSettings) {
        return toJson(writerSettings, templateCodec);
    }

    /**
     * Gets a hydrated JSON representation of this template.
     *
     * @param writerSettings the json writer settings to use when encoding
     * @param encoder the document codec instance to use to encode the document
     * @return a JSON representation of this document
     * @throws org.bson.codecs.configuration.CodecConfigurationException if the registry does not contain a codec for the document values.
     */
    public String toJson(final JsonWriterSettings writerSettings, final Encoder<Template> encoder) {
        JsonWriter writer = new JsonWriter(new StringWriter(), writerSettings);
        encoder.encode(writer, this, EncoderContext.builder().isEncodingCollectibleDocument(true).build());
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