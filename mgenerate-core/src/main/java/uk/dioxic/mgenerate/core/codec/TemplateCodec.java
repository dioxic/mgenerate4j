package uk.dioxic.mgenerate.core.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import uk.dioxic.mgenerate.core.Template;
import uk.dioxic.mgenerate.core.TemplateStateCache;
import uk.dioxic.mgenerate.core.resolver.DocumentKeyResolver;

public class TemplateCodec implements Codec<Template> {

    private static ThreadLocal<Boolean> stateCachingRequired = ThreadLocal.withInitial(() -> Boolean.FALSE);

    private DocumentCodec documentCodec;

    /**
     * Construct a new instance with a default {@code CodecRegistry}.
     */
    public TemplateCodec() {
        documentCodec = new MgenDocumentCodec();
    }

    /**
     * Enable state caching for this template. This is set during decoding by the {@link DocumentKeyResolver}
     */
    public static void enableStateCaching() {
        stateCachingRequired.set(Boolean.TRUE);
    }

    @Override
    public Template decode(BsonReader reader, DecoderContext decoderContext) {
        // determine whether the document tree contains a DocumentKeyResolver and we need document state caching.
        // the constructor of the DocumentKeyResolver sets the threadlocal flag

        stateCachingRequired.set(Boolean.FALSE);
        return Template.from(documentCodec.decode(reader, decoderContext), stateCachingRequired.get());
    }

    @Override
    public void encode(BsonWriter writer, Template template, EncoderContext encoderContext) {
        TemplateStateCache.setTemplateContext(template);
        documentCodec.encode(writer, template.getDocument(), encoderContext);
    }

    @Override
    public Class<Template> getEncoderClass() {
        return Template.class;
    }

}
