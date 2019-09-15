package uk.dioxic.mgenerate.core.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistry;
import uk.dioxic.mgenerate.core.DocumentStateCache;
import uk.dioxic.mgenerate.core.Template;

import static java.util.Arrays.asList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

public class TemplateCodec implements Codec<Template> {

    private static final CodecRegistry DEFAULT_REGISTRY = fromProviders(asList(new ValueCodecProvider(),
            new BsonValueCodecProvider(),
            new DocumentCodecProvider(new OperatorTransformer()),
            new ExtendedCodecProvider(),
            new OperatorCodecProvider(),
            new TemplateCodecProvider()));
    private static ThreadLocal<Boolean> keyResolverCount = ThreadLocal.withInitial(() -> Boolean.FALSE);

    private DocumentCodec documentCodec;


    public TemplateCodec() {
        documentCodec = new DocumentCodec(DEFAULT_REGISTRY, new BsonTypeClassMap(), new OperatorTransformer());
    }

    public static CodecRegistry getCodeRegistry() {
        return DEFAULT_REGISTRY;
    }

    public static void keyResolverFound() {
        keyResolverCount.set(Boolean.TRUE);
    }

    @Override
    public Template decode(BsonReader reader, DecoderContext decoderContext) {
        // determine whether the document tree contains a DocumentKeyResolver and we need document state caching.
        // the constructor of the DocumentKeyResolver sets the threadlocal flag

        keyResolverCount.set(Boolean.FALSE);
        Template template = new Template(documentCodec.decode(reader, decoderContext));
        template.setStateCachingRequired(keyResolverCount.get());
        return template;
    }

    @Override
    public void encode(BsonWriter writer, Template value, EncoderContext encoderContext) {
        DocumentStateCache.setEncodingContext(value);
        documentCodec.encode(writer, value.getDocument(), encoderContext);
    }

    @Override
    public Class<Template> getEncoderClass() {
        return Template.class;
    }
}
