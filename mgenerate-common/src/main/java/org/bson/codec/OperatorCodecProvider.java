package org.bson.codec;

import ch.rasc.bsoncodec.time.LocalDateTimeDateCodec;
import com.dioxic.mgenerate.Resolvable;
import org.bson.Document;
import org.bson.Transformer;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.CodeWithScope;

import java.util.HashMap;
import java.util.Map;

import static org.bson.assertions.Assertions.notNull;

public final class OperatorCodecProvider implements CodecProvider {

    private final Map<Class<?>, Codec<?>> codecs = new HashMap<>();

    /**
     * A provider of Codecs for Resolvable value types and some others.
     */
    public OperatorCodecProvider() {
        addCodecs();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(final Class<T> clazz, final CodecRegistry registry) {
        if (Resolvable.class.isAssignableFrom(clazz)) {
            return (Codec<T>) new OperatorCodec();
        }

        return (Codec<T>) codecs.get(clazz);
    }

    private void addCodecs() {
        addCodec(new LocalDateTimeDateCodec());
    }

    private <T> void addCodec(final Codec<T> codec) {
        codecs.put(codec.getEncoderClass(), codec);
    }

}