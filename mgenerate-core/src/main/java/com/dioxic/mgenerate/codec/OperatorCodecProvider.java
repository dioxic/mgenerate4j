package com.dioxic.mgenerate.codec;

import ch.rasc.bsoncodec.time.InstantInt64Codec;
import ch.rasc.bsoncodec.time.LocalDateTimeDateCodec;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import uk.dioxic.faker.resolvable.Resolvable;

import java.util.HashMap;
import java.util.Map;

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
        addCodec(new InstantInt64Codec());
    }

    private <T> void addCodec(final Codec<T> codec) {
        codecs.put(codec.getEncoderClass(), codec);
    }

}