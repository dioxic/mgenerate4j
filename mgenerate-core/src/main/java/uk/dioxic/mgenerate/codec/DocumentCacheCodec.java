package uk.dioxic.mgenerate.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.Transformer;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import uk.dioxic.mgenerate.DocumentValueCache;

public class DocumentCacheCodec extends DocumentCodec {

    public DocumentCacheCodec() {
        super();
    }

    public DocumentCacheCodec(CodecRegistry registry) {
        super(registry);
    }

    public DocumentCacheCodec(CodecRegistry registry, BsonTypeClassMap bsonTypeClassMap) {
        super(registry, bsonTypeClassMap);
    }

    public DocumentCacheCodec(CodecRegistry registry, BsonTypeClassMap bsonTypeClassMap, Transformer valueTransformer) {
        super(registry, bsonTypeClassMap, valueTransformer);
    }

    @Override
    public void encode(BsonWriter writer, Document document, EncoderContext encoderContext) {
        if (encoderContext.isEncodingCollectibleDocument()) {
            DocumentValueCache.setEncodingContext(document);
        }
        super.encode(writer, document, encoderContext);
    }

    public Document decodeAndMap(BsonReader reader, DecoderContext decoderContext) {
        Document document = super.decode(reader, decoderContext);
        DocumentValueCache.mapDocument(document);
        return document;
    }
}
