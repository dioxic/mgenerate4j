package uk.dioxic.mgenerate.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import uk.dioxic.mgenerate.util.FlsUtil;

public class PointCodec implements Codec<FlsUtil.Point> {
    @Override
    public FlsUtil.Point decode(BsonReader reader, DecoderContext decoderContext) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void encode(BsonWriter writer, FlsUtil.Point value, EncoderContext encoderContext) {
        writer.writeStartArray();
        writer.writeDouble(value.getX());
        writer.writeDouble(value.getY());
        writer.writeEndArray();
    }

    @Override
    public Class<FlsUtil.Point> getEncoderClass() {
        return FlsUtil.Point.class;
    }
}
