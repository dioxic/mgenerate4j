package uk.dioxic.mgenerate.core.codec;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.jsr310.InstantCodec;
import org.bson.codecs.jsr310.LocalDateTimeCodec;

import java.util.HashMap;
import java.util.Map;

public class ExtendedCodecProvider implements CodecProvider {

    private final Map<Class<?>, Codec<?>> codecs = new HashMap<>();

    /**
     * A provider of Codecs for extended types.
     */
    public ExtendedCodecProvider() {
        addCodecs();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        return (Codec<T>) codecs.get(clazz);
    }

    private void addCodecs() {
        addCodec(new LocalDateTimeCodec());
        addCodec(new InstantCodec());
        addCodec(new PointCodec());
    }

    private <T> void addCodec(final Codec<T> codec) {
        codecs.put(codec.getEncoderClass(), codec);
    }
}
