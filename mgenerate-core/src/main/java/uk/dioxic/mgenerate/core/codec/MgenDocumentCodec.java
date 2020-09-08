package uk.dioxic.mgenerate.core.codec;

import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.Transformer;
import org.bson.UuidRepresentation;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistry;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.core.TemplateStateCache;

import java.util.Map;

import static org.bson.codecs.configuration.CodecRegistries.*;

public class MgenDocumentCodec extends DocumentCodec {
    private static final CodecRegistry DEFAULT_REGISTRY = fromRegistries(
            fromCodecs(new UuidCodec(UuidRepresentation.STANDARD)),
            fromProviders(
                    new ExtendedCodecProvider(),
                    new ValueCodecProvider(),
                    new BsonValueCodecProvider(),
                    new MgenDocumentCodecProvider(),
                    new BsonCodecProvider()
            )
    );
    private static final BsonTypeClassMap DEFAULT_BSON_TYPE_CLASS_MAP = new BsonTypeClassMap();
    private static final String ID_FIELD_NAME = "_id";

    private final CodecRegistry registry;

    public static CodecRegistry getCodecRegistry() {
        return DEFAULT_REGISTRY;
    }

    /**
     * Construct a new instance with a default {@code CodecRegistry}.
     */
    public MgenDocumentCodec() {
        this(DEFAULT_REGISTRY);
    }

    /**
     * Construct a new instance with the given registry.
     *
     * @param registry         the registry
     */
    public MgenDocumentCodec(final CodecRegistry registry) {
        this(registry, DEFAULT_BSON_TYPE_CLASS_MAP);
    }

    /**
     * Construct a new instance with the given registry and BSON type class map.
     *
     * @param registry         the registry
     * @param bsonTypeClassMap the BSON type class map
     */
    public MgenDocumentCodec(final CodecRegistry registry, final BsonTypeClassMap bsonTypeClassMap) {
        this(registry, bsonTypeClassMap, new OperatorTransformer());
    }

    /**
     * Construct a new instance with the given registry and BSON type class map. The transformer is applied as a last step when decoding
     * values, which allows users of this codec to control the decoding process.  For example, a user of this class could substitute a
     * value decoded as a Document with an instance of a special purpose class (e.g., one representing a DBRef in MongoDB).
     *
     * @param registry         the registry
     * @param bsonTypeClassMap the BSON type class map
     * @param valueTransformer the value transformer to use as a final step when decoding the value of any field in the document
     */
    public MgenDocumentCodec(final CodecRegistry registry, final BsonTypeClassMap bsonTypeClassMap, final Transformer valueTransformer) {
        super(registry, bsonTypeClassMap, valueTransformer);
        this.registry = registry;
    }

    @Override
    public void encode(final BsonWriter writer, final Document document, final EncoderContext encoderContext) {
        writeMap(writer, document, encoderContext);
    }

    private void beforeFields(final BsonWriter bsonWriter, final EncoderContext encoderContext, final Map<String, Object> document) {
        if (encoderContext.isEncodingCollectibleDocument() && document.containsKey(ID_FIELD_NAME)) {
            bsonWriter.writeName(ID_FIELD_NAME);
            writeValue(bsonWriter, encoderContext, document.get(ID_FIELD_NAME));
        }
    }

    private boolean skipField(final EncoderContext encoderContext, final String key) {
        return encoderContext.isEncodingCollectibleDocument() && key.equals(ID_FIELD_NAME);
    }

    private Object resolve(Object o) {
        return (o instanceof Resolvable) ? TemplateStateCache.get((Resolvable<?>) o) : o;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void writeValue(final BsonWriter writer, final EncoderContext encoderContext, Object value) {
        value = resolve(value);
        if (value == null) {
            writer.writeNull();
        } else if (value instanceof Iterable) {
            writeIterable(writer, (Iterable<Object>) value, encoderContext.getChildContext());
        } else if (value instanceof Map) {
            writeMap(writer, (Map<String, Object>) value, encoderContext.getChildContext());
        } else {
            Codec codec = registry.get(value.getClass());
            encoderContext.encodeWithChildContext(codec, writer, value);
        }
    }

    private void writeMap(final BsonWriter writer, final Map<String, Object> map, final EncoderContext encoderContext) {
        writer.writeStartDocument();

        beforeFields(writer, encoderContext, map);

        for (final Map.Entry<String, Object> entry : map.entrySet()) {
            if (skipField(encoderContext, entry.getKey())) {
                continue;
            }
            Object value = entry.getValue();
            if (value instanceof Resolvable<?>) {
                value = TemplateStateCache.get((Resolvable<?>) value);
                if (value == null) {
                    continue;
                }
            }

            writer.writeName(entry.getKey());
            writeValue(writer, encoderContext, value);
        }
        writer.writeEndDocument();
    }

    private void writeIterable(final BsonWriter writer, final Iterable<Object> list, final EncoderContext encoderContext) {
        writer.writeStartArray();
        for (final Object value : list) {
            Object v = resolve(value);
            if (v != null) {
                writeValue(writer, encoderContext, v);
            }
        }
        writer.writeEndArray();
    }

}
