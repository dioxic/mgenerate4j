package uk.dioxic.mgenerate.core.codec;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import uk.dioxic.mgenerate.common.Resolvable;

public final class OperatorCodecProvider implements CodecProvider {

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(final Class<T> clazz, final CodecRegistry registry) {
        if (Resolvable.class.isAssignableFrom(clazz)) {
            return (Codec<T>) new OperatorCodec();
        }

        return null;
    }

}