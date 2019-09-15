package uk.dioxic.mgenerate.core.codec;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.core.Template;

public final class TemplateCodecProvider implements CodecProvider {

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(final Class<T> clazz, final CodecRegistry registry) {
        if (clazz == Template.class) {
            return (Codec<T>) new TemplateCodec();
        }

        return null;
    }

}