package uk.dioxic.mgenerate.core.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistry;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.core.DocumentStateCache;

import java.util.Map;

import static java.util.Arrays.asList;
import static org.bson.assertions.Assertions.notNull;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

public class OperatorCodec implements Codec<Resolvable> {

    private static final CodecRegistry DEFAULT_REGISTRY = fromProviders(asList(new ValueCodecProvider(),
            new BsonValueCodecProvider(),
            new ExtendedCodecProvider(),
            new OperatorCodecProvider()));

    private final CodecRegistry registry;

    /**
     * Construct a new instance with a default {@code CodecRegistry}.
     */
    public OperatorCodec() {
        this(DEFAULT_REGISTRY);
    }

    /**
     * Construct a new instance with the given registry.
     *
     * @param registry the registry
     */
    public OperatorCodec(final CodecRegistry registry) {
        this.registry = notNull("registry", registry);
    }

    @Override
    public Resolvable decode(BsonReader reader, DecoderContext decoderContext) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void encode(BsonWriter writer, Resolvable value, EncoderContext encoderContext) {
        writeValue(writer, encoderContext, DocumentStateCache.get(value));
    }

    @Override
    public Class<Resolvable> getEncoderClass() {
        return Resolvable.class;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void writeValue(final BsonWriter writer, final EncoderContext encoderContext, final Object value) {
        if (value == null) {
            writer.writeNull();
        }
        else if (value instanceof Iterable) {
            writeIterable(writer, (Iterable<Object>) value, encoderContext.getChildContext());
        }
        else if (value instanceof Map) {
            writeMap(writer, (Map<String, Object>) value, encoderContext.getChildContext());
        }
        else {
            Codec codec = registry.get(value.getClass());
            encoderContext.encodeWithChildContext(codec, writer, value);
        }
    }

    private void writeMap(final BsonWriter writer, final Map<String, Object> map, final EncoderContext encoderContext) {
        writer.writeStartDocument();

        for (final Map.Entry<String, Object> entry : map.entrySet()) {
            writer.writeName(entry.getKey());
            writeValue(writer, encoderContext, entry.getValue());
        }
        writer.writeEndDocument();
    }

    private void writeIterable(final BsonWriter writer, final Iterable<Object> list, final EncoderContext encoderContext) {
        writer.writeStartArray();
        for (final Object value : list) {
            writeValue(writer, encoderContext, value);
        }
        writer.writeEndArray();
    }

}
